package com.marketplace.authorizationserver.controller;

import com.marketplace.authorizationserver.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/private/api/v1/")
@RequiredArgsConstructor
public class UserAuthController {
    private final AuthService authService;

    @GetMapping("authentication/{email}")
    public String getAuthUserExternalId(@PathVariable String email) {
        log.info("Getting external id");
        return authService.getExternalId(email);
    }
}