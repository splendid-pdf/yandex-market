package com.yandex.market.userinfoservice.controller.publicapi;

import com.yandex.market.userinfoservice.dto.UserRegistrationDto;
import com.yandex.market.userinfoservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/api/v1/users")
@Tag(name = "public")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Invalid data provided to the server",
                content = @Content(mediaType = "application/json"))})
public class SignUpUserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "signUp", summary = "Registration user in application")
    @ApiResponse(responseCode = "200", description = "Successful")
    @PostMapping("/signup")
    public void signUp(@Parameter(name = "userRegistrationDto", description = "User registration")
            @RequestBody UserRegistrationDto userRegistrationDto) {
        userService.signUp(userRegistrationDto);
    }
}