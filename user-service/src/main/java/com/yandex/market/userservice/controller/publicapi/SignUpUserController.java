package com.yandex.market.userservice.controller.publicapi;

import com.yandex.market.userservice.dto.request.UserRegistrationDto;
import com.yandex.market.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

@RestController
@RequiredArgsConstructor
@RequestMapping(PUBLIC_API_V1)
@Tag(name = "public")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Invalid data provided to the server",
                content = @Content(mediaType = "application/json"))})
public class SignUpUserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "signUp", summary = "Registration user in application")
    @ApiResponse(responseCode = "200", description = "Successful")
    @PostMapping("/users/signup")
    public UUID signUp(@Parameter(name = "userRegistrationDto", description = "User registration")
            @RequestBody UserRegistrationDto userRegistrationDto) {
        return userService.signUp(userRegistrationDto);
    }
}