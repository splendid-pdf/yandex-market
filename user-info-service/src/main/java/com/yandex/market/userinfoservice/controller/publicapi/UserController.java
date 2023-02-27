package com.yandex.market.userinfoservice.controller.publicapi;

import com.yandex.market.userinfoservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.controller.PublicApi;
import org.openapitools.api.model.UserRequestDto;
import org.openapitools.api.model.UserResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class UserController implements PublicApi {

    private final UserService userService;

    @PreAuthorize("@userAuthenticationManager.externalIdMatches(authentication, #externalId)")
    @Override
    public ResponseEntity<Void> deleteUser(UUID externalId) {
        log.info("Received request to delete a user by externalId: {}", externalId);
        userService.deleteUserByExternalId(externalId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("@userAuthenticationManager.externalIdMatches(authentication, #externalId)")
    @Override
    public ResponseEntity<UserResponseDto> getByExternalId(UUID externalId) {
        log.info("Received request to get a user by externalId: {}", externalId);
        return ResponseEntity.ok(userService.findUserByExternalId(externalId));
    }


    @PreAuthorize("@userAuthenticationManager.externalIdMatchesByEmailOrPhone(authentication, #emailOrPhone)")
    @Override
    public ResponseEntity<UserResponseDto> getUserByEmailOrPhone(String emailOrPhone) {
        log.info("Received request to get a user by given value: {}", emailOrPhone);
        return ResponseEntity.ok(userService.getUserDtoByEmailOrPhone(emailOrPhone));
    }

    @PreAuthorize("@userAuthenticationManager.externalIdMatches(authentication, #externalId)")
    @Override
    public ResponseEntity<UserResponseDto> updateUser(UUID externalId, UserRequestDto userRequestDto) {
        log.info("Received request to update a user: {}", userRequestDto);
        return ResponseEntity.ok(userService.update(externalId, userRequestDto));
    }
}