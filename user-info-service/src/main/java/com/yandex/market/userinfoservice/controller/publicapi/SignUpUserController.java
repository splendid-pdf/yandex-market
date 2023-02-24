package com.yandex.market.userinfoservice.controller.publicapi;

import com.yandex.market.userinfoservice.dto.UserRegistrationDto;
import com.yandex.market.userinfoservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/api/v1/users")
public class SignUpUserController {

    private final UserService userService;

    @PostMapping("/signup")
    public void signUp(@RequestBody UserRegistrationDto userRegistrationDto) {
        userService.signUp(userRegistrationDto);
    }
}