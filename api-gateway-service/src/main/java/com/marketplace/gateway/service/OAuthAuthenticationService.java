package com.marketplace.gateway.service;

import com.marketplace.gateway.dto.OAuthUserResponse;
import com.marketplace.gateway.dto.OAuthUser;
import com.marketplace.gateway.dto.UserLoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static com.marketplace.gateway.util.AuthConstants.X_USER_ID_HEADER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthAuthenticationService {
    private static final Map<String, OAuthUser> AUTHORIZED_CLIENTS_DATA = new ConcurrentHashMap<>(256);

    private final WebClient webClient;

    @Value(value = "${spring.security.oauth2.client.provider.custom-oidc.issuer-uri}")
    private String baseAuthUrl;

    public Mono<OAuthUserResponse> authenticate(Mono<UserLoginRequest> request, ServerWebExchange exchange) {
        return webClient.post()
                .uri(baseAuthUrl + "/public/api/v1/oauth/token")
                .body(request, UserLoginRequest.class)
                .exchangeToMono(this::getAuthenticatedUserData)
                .doOnNext(oAuthUser -> addAuthDataToResponseHeaders(oAuthUser, exchange))
                .doOnNext(data -> AUTHORIZED_CLIENTS_DATA.put(data.id(), data))
                .doOnNext(data -> log.info(AUTHORIZED_CLIENTS_DATA.toString()))
                .map(data -> new OAuthUserResponse(data.id(), data.email()));
    }

    public Mono<ResponseEntity<Void>> deauthenticate(ServerHttpRequest request) {
        return Mono.defer(() -> containsAuthorizationAndUserIdHeaders(request)
                        ? Mono.fromSupplier(() -> sendRevocationRequest(request))
                            .flatMap(Function.identity())
                            .doOnNext(responseEntity ->
                                AUTHORIZED_CLIENTS_DATA.remove(request.getHeaders().getFirst(X_USER_ID_HEADER)))
                            .doOnNext(data -> log.info(AUTHORIZED_CLIENTS_DATA.toString()))
                        : Mono.error(() -> new AuthorizationServiceException("User is not authenticated")));
    }

    public boolean isAuthenticated(String userId, String token) {
        OAuthUser authenticatedUser = AUTHORIZED_CLIENTS_DATA.get(userId);
        return authenticatedUser != null
                && authenticatedUser.accessTokenExpiredAt().isAfter(Instant.now())
                && token.equals(authenticatedUser.accessToken());
    }

    private Mono<OAuthUser> getAuthenticatedUserData(ClientResponse clientResponse) {
        if (clientResponse.statusCode().is2xxSuccessful()) {
            return clientResponse.bodyToMono(OAuthUser.class);
        }
        return Mono.error(() -> new AuthorizationServiceException("User was not authenticated"));
    }

    private void addAuthDataToResponseHeaders(OAuthUser data, ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getResponse().getHeaders();
        headers.add(AUTHORIZATION, "%s %s".formatted(BEARER.getValue(), data.accessToken()));
        headers.add(X_USER_ID_HEADER, data.id());
    }

    private static boolean containsAuthorizationAndUserIdHeaders(ServerHttpRequest request) {
        return StringUtils.hasText(request.getHeaders().getFirst(AUTHORIZATION))
                && StringUtils.hasText(request.getHeaders().getFirst(X_USER_ID_HEADER));
    }

    private Mono<ResponseEntity<Void>> sendRevocationRequest(ServerHttpRequest request) {
        return webClient.post()
                .uri(baseAuthUrl + "/public/api/v1/oauth/revoke")
                .header(AUTHORIZATION, request.getHeaders().getFirst(AUTHORIZATION))
                .exchangeToMono(this::handleRevocationResponse);
    }

    private Mono<ResponseEntity<Void>> handleRevocationResponse(ClientResponse clientResponse) {
        if (!clientResponse.statusCode().is2xxSuccessful()) {
            return Mono.error(() -> new AuthorizationServiceException("User is not authenticated"));
        }

        return clientResponse.toBodilessEntity();
    }
}