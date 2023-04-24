package com.marketplace.userservice.controller.publicapi;

import com.marketplace.userservice.dto.request.UserRegistrationDto;
import com.marketplace.userservice.dto.request.UserRequestDto;
import com.marketplace.userservice.dto.response.CreateUserResponse;
import com.marketplace.userservice.dto.response.UserResponseDto;
import com.marketplace.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

@Slf4j
@RestController
@RequestMapping(PUBLIC_API_V1)
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUserResponse create(@RequestBody UserRegistrationDto userRegistrationDto) {
        return userService.signUp(userRegistrationDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{externalId}")
    public UserResponseDto getByExternalId(@PathVariable("externalId") UUID externalId) {
        log.info("Received request to get a user by id: {}", externalId);
        return userService.findUserByExternalId(externalId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users/{externalId}")
    public UserResponseDto updateUser(@PathVariable("externalId") UUID externalId,
                                      @Valid @RequestBody(required = false) UserRequestDto userRequestDto) {
        log.info("Received request to update a user: {}", userRequestDto);
        return userService.update(externalId, userRequestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{externalId}")
    public void deleteUser(@PathVariable("externalId") UUID externalId) {
        log.info("Received request to delete a user by id: {}", externalId);
        userService.deleteUserByExternalId(externalId);
    }
}