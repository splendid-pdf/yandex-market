package com.marketplace.gateway.controller;

import com.marketplace.gateway.dto.OAuthUserResponse;
import com.marketplace.gateway.dto.UserLoginRequest;
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
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/public/api/v1/oauth")
@RequiredArgsConstructor
@ApiResponses(value = {@ApiResponse(responseCode = "401", description = "Unauthorized")})
public class SignController {

    private final OAuthAuthenticationService authenticationService;

    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Logged In"),
    })
    public Mono<OAuthUserResponse> login(@RequestBody Mono<UserLoginRequest> request,
                                         ServerWebExchange exchange) {
        return authenticationService.authenticate(request, exchange);
    }

    @PostMapping("/logout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully Logged out")
    })
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", required = true),
            @Parameter(in = ParameterIn.HEADER, name = "X-User-Id", required = true)
    })
    public Mono<ResponseEntity<Void>> logout(ServerHttpRequest request) {
        return authenticationService.deauthenticate(request);
    }
}