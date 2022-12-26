package com.yandex.market.userinfoservice.controller;

import com.yandex.market.userinfoservice.dto.UserDto;
import com.yandex.market.userinfoservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("${app.users-api.path}")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID registrationUser(@RequestBody @Valid UserDto userDto){
        log.info("Received a request to create a user: {}", userDto);
        return userService.create(userDto);
    }

}
