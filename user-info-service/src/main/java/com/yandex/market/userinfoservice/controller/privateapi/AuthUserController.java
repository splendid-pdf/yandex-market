package com.yandex.market.userinfoservice.controller.privateapi;

import com.yandex.market.userinfoservice.dto.UserAuthenticationDto;
import com.yandex.market.userinfoservice.service.UserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/private/api/v1/users/auth-details")
public class AuthUserController {

    private final UserSearchService userSearchService;

    @GetMapping("/{email}")
    public UserAuthenticationDto authenticate(@PathVariable("email") String email) {
        return userSearchService.authenticate(email);
    }
}