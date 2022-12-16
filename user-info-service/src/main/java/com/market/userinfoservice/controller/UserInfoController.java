package com.market.userinfoservice.controller;

import com.market.userinfoservice.model.User;
import com.market.userinfoservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("${spring.application.url}")
@RequiredArgsConstructor
public class UserInfoController {
    private final UserService userService;
    @Value("${spring.application.url}")
    private String REQUEST_NAME;

    @GetMapping("/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserData(@PathVariable("externalId") UUID externalId) {
        log.info("GET %s/%s".formatted(REQUEST_NAME, externalId));
        return userService.findUserByExternalId(externalId);
    }

    @DeleteMapping("/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable("externalId") UUID externalId) {
        log.info("DELETE %s/%s".formatted(REQUEST_NAME, externalId));
        userService.deleteUserByExternalId(externalId);
    }
}
