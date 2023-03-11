package com.marketplace.gateway.controller;

import com.marketplace.gateway.dto.OAuthUser;
import com.marketplace.gateway.dto.OAuthUserData;
import com.marketplace.gateway.dto.UserLoginRequest;
import com.marketplace.gateway.dto.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

@RestController
@RequestMapping("/public/api/v1/oauth")
@RequiredArgsConstructor
public class SignController {

    private final WebClient webClient;

    @Value(value = "${spring.security.oauth2.client.provider.custom-oidc.issuer-uri}")
    private String baseAuthUrl;

    @Value(value = "${app.urls.users}")
    private String baseUsersUrl;

    @PostMapping("/login")
    public Mono<OAuthUser> login(@RequestBody Mono<UserLoginRequest> request, ServerWebExchange exchange) {
        return webClient.post()
                .uri(baseAuthUrl + "/public/api/v1/oauth/token")
                .body(request, UserLoginRequest.class)
                .exchangeToMono(clientResponse -> getAuthenticatedUserData(exchange, clientResponse))
                .map(data -> new OAuthUser(data.id(), data.email()));
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<Void>> logout(ServerHttpRequest request) {
        return Mono.defer(() ->
                !CollectionUtils.isEmpty(request.getHeaders().get(AUTHORIZATION))
                        ? Mono.fromSupplier(() -> sendRevocationRequest(request))
                            .flatMap(Function.identity())
                        : Mono.error(() -> new AuthorizationServiceException("User is not authenticated")));
    }

    @PostMapping("/signup")
    public Mono<UUID> signup(@RequestBody Mono<UserRegistrationRequest> request) {
        return webClient.post()
                .uri(baseUsersUrl + "/public/api/v1/oauth/token")
                .body(request, UserRegistrationRequest.class)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(UUID.class))
                .log();
    }

    private Mono<OAuthUserData> getAuthenticatedUserData(ServerWebExchange exchange, ClientResponse clientResponse) {
        if (clientResponse.statusCode().is2xxSuccessful()) {
            return clientResponse.bodyToMono(OAuthUserData.class)
                    .doOnNext(oAuthUserData -> exchange.getResponse()
                            .getHeaders()
                            .add(AUTHORIZATION, "%s %s".formatted(BEARER.getValue(), oAuthUserData.accessToken())));
        }
        return Mono.error(() -> new AuthorizationServiceException("User was not authenticated"));
    }

    private Mono<ResponseEntity<Void>> sendRevocationRequest(ServerHttpRequest request) {
        return webClient.post()
                .uri(baseAuthUrl + "/public/api/v1/oauth/revoke")
                .header(AUTHORIZATION, Stream.ofNullable(request.getHeaders().get(AUTHORIZATION))
                        .flatMap(List::stream)
                        .findFirst()
                        .orElse(null))
                .exchangeToMono(this::handleRevocationResponse);
    }

    private Mono<ResponseEntity<Void>> handleRevocationResponse(ClientResponse clientResponse) {
        if (!clientResponse.statusCode().is2xxSuccessful()) {
            return Mono.error(() -> new AuthorizationServiceException("User is not authenticated"));
        }

        return clientResponse.toBodilessEntity();
    }
}