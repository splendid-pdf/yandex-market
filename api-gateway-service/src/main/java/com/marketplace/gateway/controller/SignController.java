package com.marketplace.gateway.controller;

import com.marketplace.gateway.dto.OAuthClientResponse;
import com.marketplace.gateway.dto.LoginRequest;
import com.marketplace.gateway.service.OAuthAuthenticationService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/public/api/v1")
@RequiredArgsConstructor
@ApiResponses(value = {@ApiResponse(responseCode = "401", description = "Unauthorized")})
public class SignController {

    private final OAuthAuthenticationService authenticationService;

    @PostMapping("/users/oauth/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged in"),
    })
    public Mono<OAuthClientResponse> loginUser(@RequestBody Mono<LoginRequest> request) {
        return authenticationService.authenticateUser(request);
    }

    @PostMapping("/sellers/oauth/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seller successfully logged in"),
    })
    public Mono<OAuthClientResponse> loginSeller(@RequestBody Mono<LoginRequest> request) {
        return authenticationService.authenticateSeller(request);
    }

    @PostMapping("/oauth/logout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully Logged out")
    })
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", required = true)
    })
    public Mono<ResponseEntity<Void>> logout(ServerHttpRequest request) {
        return authenticationService.deauthenticate(request);
    }
}