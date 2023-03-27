package com.yandex.market.userservice.controller.publicapi;

import com.yandex.market.userservice.dto.request.UserRegistrationDto;
import com.yandex.market.userservice.dto.request.UserRequestDto;
import com.yandex.market.userservice.dto.response.UserResponseDto;
import com.yandex.market.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @ResponseStatus(HttpStatus.OK)
    public UUID create(@RequestBody UserRegistrationDto userRegistrationDto) {
        return userService.signUp(userRegistrationDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{externalId}")
    public UserResponseDto getByExternalId(@PathVariable("externalId") UUID externalId) {
        log.info("Received request to get a user by externalId: {}", externalId);
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
        log.info("Received request to delete a user by externalId: {}", externalId);
        userService.deleteUserByExternalId(externalId);
    }
}