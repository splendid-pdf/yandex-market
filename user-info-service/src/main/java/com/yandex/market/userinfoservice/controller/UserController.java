package com.yandex.market.userinfoservice.controller;

import com.yandex.market.userinfoservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.controller.UsersApi;
import org.openapitools.api.model.LocationDto;
import org.openapitools.api.model.UserRequestDto;
import org.openapitools.api.model.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    private final UserService userService;


    @Override
    public ResponseEntity<UUID> createUser(UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.create(userRequestDto));
    }

    @Override
    public ResponseEntity<UserResponseDto> deleteUser(UUID externalId) {
        return ResponseEntity.ok(userService.deleteUserByExternalId(externalId));
    }

    @Override
    public ResponseEntity<UserResponseDto> getByExternalId(UUID externalId) {
        return ResponseEntity.ok(userService.findUserByExternalId(externalId));
    }

    @Override
    public ResponseEntity<UserResponseDto> getUserByEmailOrPhone(String emailOrPhone) {
        return UsersApi.super.getUserByEmailOrPhone(emailOrPhone);
    }

    @Override
    public ResponseEntity<LocationDto> getUserLocationByExternalId(UUID externalId) {
        return UsersApi.super.getUserLocationByExternalId(externalId);
    }

    @Override
    public ResponseEntity<UserResponseDto> updateUser(UUID externalId, UserRequestDto userRequestDto) {
        log.info("Received request to update a user: {}", userRequestDto);
        return ResponseEntity.ok(userService.update(externalId, userRequestDto));
    }

}
