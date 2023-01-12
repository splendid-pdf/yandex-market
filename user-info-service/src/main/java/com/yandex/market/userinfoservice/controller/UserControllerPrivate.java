package com.yandex.market.userinfoservice.controller;

import com.yandex.market.userinfoservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.controller.PrivateApi;
import org.openapitools.api.model.UserFilter;
import org.openapitools.api.model.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;
@RestController
@RequiredArgsConstructor
public class UserControllerPrivate implements PrivateApi {

    private final UserService userService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return PrivateApi.super.getRequest();
    }

    @Override
    public ResponseEntity<List<UserResponseDto>> getUsersByFilter(UserFilter userFilter) {
        return ResponseEntity.ok(userService.getUsersByFilter(userFilter));
    }
}
