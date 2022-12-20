package com.yandex.market.userinfoservice.controller;

import org.openapitools.api.controller.UsersApi;
import org.openapitools.api.model.LocationDto;
import org.openapitools.api.model.UserRequestDto;
import org.openapitools.api.model.UserResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class UserController implements UsersApi {

    @Override
    public ResponseEntity<UUID> createUser(UserRequestDto userRequestDto) {
        return UsersApi.super.createUser(userRequestDto);
    }

    @Override
    public ResponseEntity<UserResponseDto> deleteUser(UUID externalId) {
        return UsersApi.super.deleteUser(externalId);
    }

    @Override
    public ResponseEntity<UserResponseDto> getByExternalId(UUID externalId) {
        return UsersApi.super.getByExternalId(externalId);
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
        return UsersApi.super.updateUser(externalId, userRequestDto);
    }
}