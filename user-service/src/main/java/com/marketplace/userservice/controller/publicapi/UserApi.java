package com.marketplace.userservice.controller.publicapi;

import com.marketplace.userservice.dto.request.UserRegistrationDto;
import com.marketplace.userservice.dto.request.UserRequestDto;
import com.marketplace.userservice.dto.response.CreateUserResponse;
import com.marketplace.userservice.dto.response.UserResponseDto;
import com.yandex.market.model.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;

@Tag(name = "users")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid data provided to the server", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        }),
        @ApiResponse(responseCode = "401", description = "Unauthorized error"),
        @ApiResponse(responseCode = "403", description = "Forbidden to get a resource"),
        @ApiResponse(responseCode = "404", description = "User by id {id} was not found", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        })
})
public interface UserApi {

    @Operation(
            summary = "Register a user",
            responses = @ApiResponse(responseCode = "201", description = "Created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserResponse.class))}
            ))
    CreateUserResponse create(
            @Parameter(name = "userRegistrationDto", description = "User registration")
            UserRegistrationDto userRegistrationDto
    );

    @Operation(
            summary = "Get a user by external id", security = @SecurityRequirement(name = "bearerAuth"),
            responses = @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))
            }))
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #externalId,
                T(com.yandex.market.auth.model.Role).USER,
                T(com.yandex.market.auth.util.ClientAttributes).USER_ID
            )
            """
    )
    UserResponseDto getByExternalId(
            @Parameter(name = "id", description = "User External Id", required = true) UUID externalId);


    @Operation(
            summary = "Update user", security = @SecurityRequirement(name = "bearerAuth"),
            responses = @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))
            }))
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #externalId,
                T(com.yandex.market.auth.model.Role).USER,
                T(com.yandex.market.auth.util.ClientAttributes).USER_ID
            )
            """
    )
    UserResponseDto updateUser(
            @Parameter(name = "id", description = "user external id", required = true) UUID externalId,
            @Parameter(name = "UserRequestDto", description = "Update an existent user") UserRequestDto userRequestDto);

    @Operation(
            summary = "Delete user", security = @SecurityRequirement(name = "bearerAuth"),
            responses = @ApiResponse(responseCode = "204", description = "Successfully deleted")
    )
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #externalId,
                T(com.yandex.market.auth.model.Role).USER,
                T(com.yandex.market.auth.util.ClientAttributes).USER_ID
            )
            """
    )
    void deleteUser(
            @Parameter(name = "id", description = "User External Id", required = true) UUID externalId
    );
}
