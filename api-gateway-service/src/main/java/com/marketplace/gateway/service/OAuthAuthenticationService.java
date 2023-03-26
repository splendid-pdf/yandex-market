package com.marketplace.gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.gateway.dto.OAuthClientResponse;
import com.marketplace.gateway.dto.OAuthClient;
import com.marketplace.gateway.dto.LoginRequest;
import com.marketplace.gateway.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static com.yandex.market.auth.util.ClientAttributes.SELLER_ID;
import static com.yandex.market.auth.util.ClientAttributes.USER_ID;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthAuthenticationService {
    private static final Map<String, OAuthClient> AUTHORIZED_CLIENTS_DATA = new ConcurrentHashMap<>(256);
    private static final String CLIENT_IS_NOT_AUTHENTICATED_MESSAGE = "Client is not authenticated";

    private final ObjectMapper objectMapper;
    private final WebClient webClient;


    @Value(value = "${spring.security.oauth2.client.provider.custom-oidc.issuer-uri}")
    private String baseAuthUrl;

    public Mono<OAuthClientResponse> authenticateUser(Mono<LoginRequest> request) {
        return webClient.post()
                .uri(baseAuthUrl + "/public/api/v1/users/oauth/token")
                .body(request, LoginRequest.class)
                .exchangeToMono(this::getAuthenticatedClientData)
                .doOnNext(data -> AUTHORIZED_CLIENTS_DATA.put(data.id(), data))
                .map(data -> new OAuthClientResponse(data.id(), data.email(), data.accessToken()));
    }

    public Mono<OAuthClientResponse> authenticateSeller(Mono<LoginRequest> request) {
        return webClient.post()
                .uri(baseAuthUrl + "/public/api/v1/sellers/oauth/token")
                .body(request, LoginRequest.class)
                .exchangeToMono(this::getAuthenticatedClientData)
                .doOnNext(data -> AUTHORIZED_CLIENTS_DATA.put(data.id(), data))
                .map(data -> new OAuthClientResponse(data.id(), data.email(), data.accessToken()));
    }

    public Mono<ResponseEntity<Void>> deauthenticate(ServerHttpRequest request) {
        String accessToken = Optional.ofNullable(request.getHeaders().getFirst(AUTHORIZATION))
                .map(token -> token.replaceFirst(OAuth2AccessToken.TokenType.BEARER.getValue(), "").trim())
                .orElseThrow(() -> new AuthenticationException("Authorization token is not provided"));
        String id = getClientId(accessToken);
        return Mono.defer(() -> StringUtils.isNotBlank(accessToken) && isAuthenticated(accessToken, id)
                        ? Mono.fromSupplier(() -> sendRevocationRequest(request))
                            .flatMap(Function.identity())
                            .doOnNext(responseEntity -> AUTHORIZED_CLIENTS_DATA.remove(id))
                            .doOnNext(data -> log.info(AUTHORIZED_CLIENTS_DATA.toString()))
                        : Mono.error(() -> new AuthenticationException(CLIENT_IS_NOT_AUTHENTICATED_MESSAGE)));
    }

    public boolean isAuthenticated(String token) {
        String id = getClientId(token);
        return isAuthenticated(token, id);
    }

    private boolean isAuthenticated(String token, String clientId) {
        if (StringUtils.isNotBlank(clientId)) {
            OAuthClient authenticatedUser = AUTHORIZED_CLIENTS_DATA.get(clientId);
            return authenticatedUser != null
                    && authenticatedUser.accessTokenExpiredAt().isAfter(Instant.now())
                    && token.equals(authenticatedUser.accessToken());
        }

        return false;
    }

    private String getClientId(String token) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        Map<String, Object> payload = Arrays.stream(token.split("\\."))
                .skip(1)
                .map(decoder::decode)
                .map(String::new)
                .map(this::convertToMap)
                .findFirst()
                .orElseThrow(() -> new AuthenticationException(CLIENT_IS_NOT_AUTHENTICATED_MESSAGE));
        return (String) (payload.containsKey(USER_ID)
                    ? payload.get(USER_ID)
                    : payload.getOrDefault(SELLER_ID, StringUtils.EMPTY));
    }

    private Mono<OAuthClient> getAuthenticatedClientData(ClientResponse clientResponse) {
        if (clientResponse.statusCode().is2xxSuccessful()) {
            return clientResponse.bodyToMono(OAuthClient.class);
        }
        return Mono.error(() -> new AuthenticationException(CLIENT_IS_NOT_AUTHENTICATED_MESSAGE));
    }

    private Mono<ResponseEntity<Void>> sendRevocationRequest(ServerHttpRequest request) {
        return webClient.post()
                .uri(baseAuthUrl + "/public/api/v1/oauth/revoke")
                .header(AUTHORIZATION, request.getHeaders().getFirst(AUTHORIZATION))
                .exchangeToMono(this::handleRevocationResponse);
    }

    private Mono<ResponseEntity<Void>> handleRevocationResponse(ClientResponse clientResponse) {
        if (!clientResponse.statusCode().is2xxSuccessful()) {
            return Mono.error(() -> new AuthenticationException(CLIENT_IS_NOT_AUTHENTICATED_MESSAGE));
        }

        return clientResponse.toBodilessEntity();
    }

    private Map<String, Object> convertToMap(String payload) {
        try {
            return objectMapper.readValue(payload, new TypeReference<HashMap<String, Object>>() {});
        } catch (JsonProcessingException e) {
            log.error("Could not parse the payload - '{}'", payload);
            throw new AuthenticationException("Invalid token has been provided");
        }
    }
}