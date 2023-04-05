package com.yandex.market.userservice.controller.privateapi;

import com.yandex.market.auth.dto.ClientAuthDetails;
import com.yandex.market.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("/private/api/v1/users/auth")
public class AuthUserController {

    private final UserService userService;

    @GetMapping
    public ClientAuthDetails authenticate(@RequestParam String email) {
        return userService.getUserDetails(email);
    }
}