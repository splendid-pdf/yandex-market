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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class UserController implements PublicApi {

    private final UserService userService;

    @PreAuthorize("@permissionService.hasPermission(#externalId)")
    @Override
    public ResponseEntity<Void> deleteUser(UUID externalId) {
        log.info("Received request to delete a user by externalId: {}", externalId);
        userService.deleteUserByExternalId(externalId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("@permissionService.hasPermission(#externalId)")
    @Override
    public ResponseEntity<UserResponseDto> getByExternalId(@PathVariable("externalId") UUID externalId) {
        log.info("Received request to get a user by externalId: {}", externalId);
        return ResponseEntity.ok(userService.findUserByExternalId(externalId));
    }

    @PreAuthorize("@permissionService.hasPermission(#externalId)")
    @Override
    public ResponseEntity<UserResponseDto> updateUser(UUID externalId, UserRequestDto userRequestDto) {
        log.info("Received request to update a user: {}", userRequestDto);
        return ResponseEntity.ok(userService.update(externalId, userRequestDto));
    }
}