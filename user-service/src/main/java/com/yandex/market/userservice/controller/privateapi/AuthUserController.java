package com.yandex.market.userservice.controller.privateapi;

import com.yandex.market.userservice.dto.request.UserAuthenticationDto;
import com.yandex.market.userservice.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "private")
@RequiredArgsConstructor
@RequestMapping("/private/api/v1/users/auth-details")
public class AuthUserController {

    private final UserService userService;

    @GetMapping("/{email}")
    public UserAuthenticationDto authenticate(@PathVariable("email") String email) {
        return userService.authenticate(email);
    }
}