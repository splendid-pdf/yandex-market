package com.marketplace.gateway.controller;

import com.marketplace.gateway.dto.OAuthUserResponse;
import com.marketplace.gateway.dto.UserLoginRequest;
import com.marketplace.gateway.service.OAuthAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/public/api/v1/oauth")
@RequiredArgsConstructor
public class SignController {

    private final OAuthAuthenticationService authenticationService;

    @PostMapping("/login")
    public Mono<OAuthUserResponse> login(@RequestBody Mono<UserLoginRequest> request, ServerWebExchange exchange) {
        return authenticationService.authenticate(request, exchange);
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<Void>> logout(ServerHttpRequest request) {
        return authenticationService.deauthenticate(request);
    }
}