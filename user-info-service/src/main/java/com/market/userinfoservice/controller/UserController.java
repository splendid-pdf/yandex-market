package com.market.userinfoservice.controller;

import com.market.userinfoservice.dto.UserDto;
import com.market.userinfoservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/public/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registrationUser(@RequestBody UserDto userDto){
        log.info("Received a request to create a user: {}", userDto);
        userService.create(userDto);
    }

}
