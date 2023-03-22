package com.yandex.market.userservice.controller.publicapi;

import com.yandex.market.userservice.dto.request.UserRequestDto;
import com.yandex.market.userservice.dto.response.ErrorResponse;
import com.yandex.market.userservice.dto.response.UserResponseDto;
import com.yandex.market.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "public")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid data provided to the server", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        }),
        @ApiResponse(responseCode = "401", description = "Unauthorized error"),
        @ApiResponse(responseCode = "403", description = "Forbidden to get a resource"),
        @ApiResponse(responseCode = "404", description = "User by id {externalId} was not found", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        })
})
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get a user by external id", security = @SecurityRequirement(name = "bearerAuth"),
            responses = @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))
            }))
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{externalId}")
    @PreAuthorize("@permissionService.hasPermission(#externalId)")
    public UserResponseDto getByExternalId(
            @Parameter(name = "externalId", description = "User External Id", required = true)
            @PathVariable("externalId") UUID externalId) {
        log.info("Received request to get a user by externalId: {}", externalId);
        return userService.findUserByExternalId(externalId);
    }

    @Operation(
            summary = "Update user", security = @SecurityRequirement(name = "bearerAuth"),
            responses = @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))
            }))
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users/{externalId}")
    @PreAuthorize("@permissionService.hasPermission(#externalId)")
    public UserResponseDto updateUser(
            @Parameter(name = "externalId", description = "user external id", required = true)
            @PathVariable("externalId") UUID externalId,
            @Parameter(name = "UserRequestDto", description = "Update an existent user")
            @Valid @RequestBody(required = false) UserRequestDto userRequestDto) {
        log.info("Received request to update a user: {}", userRequestDto);
        return userService.update(externalId, userRequestDto);
    }

    @Operation(
            summary = "Delete user", security = @SecurityRequirement(name = "bearerAuth"),
            responses = @ApiResponse(responseCode = "204", description = "Successfully deleted")
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{externalId}")
    @PreAuthorize("@permissionService.hasPermission(#externalId)")
    public void deleteUser(
            @Parameter(name = "externalId", description = "User External Id", required = true)
            @PathVariable("externalId") UUID externalId
    ) {
        log.info("Received request to delete a user by externalId: {}", externalId);
        userService.deleteUserByExternalId(externalId);
    }
}