package com.marketplace.authorizationserver.controller;

import com.marketplace.authorizationserver.dto.OAuthUserData;
import com.marketplace.authorizationserver.dto.UserLoginRequest;
import com.marketplace.authorizationserver.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("public/api/v1/oauth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    public OAuthUserData login(@RequestBody UserLoginRequest request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/revoke")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeAuth(@RequestHeader HttpHeaders headers) {
        String token = Stream.ofNullable(headers.get(AUTHORIZATION)).flatMap(List::stream).findFirst().orElse(null);
        authenticationService.revokeAuthentication(token, OAuth2TokenType.ACCESS_TOKEN);
        headers.remove(AUTHORIZATION);
    }
}