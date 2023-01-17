package com.yandex.market.userinfoservice.controller;

import com.yandex.market.userinfoservice.service.PublicUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.controller.PublicApi;
import org.openapitools.api.model.UserRequestDto;
import org.openapitools.api.model.UserResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PublicUserController implements PublicApi {

    private final PublicUserService publicUserService;

    @Override
    public ResponseEntity<UUID> createUser(UserRequestDto userRequestDto) {
        log.info("Received request to create a user: {}", userRequestDto);
        return ResponseEntity.ok(publicUserService.create(userRequestDto));
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID externalId) {
        log.info("Received request to delete a user by externalId: {}", externalId);
        publicUserService.deleteUserByExternalId(externalId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<UserResponseDto> getByExternalId(UUID externalId) {
        log.info("Received request to get a user by externalId: {}", externalId);
        return ResponseEntity.ok(publicUserService.findUserByExternalId(externalId));
    }


    @Override
    public ResponseEntity<UserResponseDto> getUserByEmailOrPhone(String emailOrPhone) {
        log.info("Received request to get a user by given value: {}", emailOrPhone);
        return ResponseEntity.ok(publicUserService.getUserDtoByEmailOrPhone(emailOrPhone));
    }

    @Override
    public ResponseEntity<UserResponseDto> updateUser(UUID externalId, UserRequestDto userRequestDto) {
        log.info("Received request to update a user: {}", userRequestDto);
        return ResponseEntity.ok(publicUserService.update(externalId, userRequestDto));
    }

}
