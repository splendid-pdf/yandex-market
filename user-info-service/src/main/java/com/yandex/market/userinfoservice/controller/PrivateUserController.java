package com.yandex.market.userinfoservice.controller;

import com.yandex.market.userinfoservice.service.UserSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.controller.PrivateApi;
import org.openapitools.api.model.UserFilter;
import org.openapitools.api.model.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PrivateUserController implements PrivateApi {

    private final UserSearchService userSearchService;

    @Override
    public ResponseEntity<List<UserResponseDto>> getUsersByFilter(UserFilter userFilter) {
        log.info("Received request to get users by filter(s): {}", userFilter);
        return ResponseEntity.ok(userSearchService.getUsersByFilter(userFilter));
    }
}
