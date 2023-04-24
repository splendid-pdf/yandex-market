package com.yandex.market.favoritesservice.controller;

import com.yandex.market.favoritesservice.dto.FavoriteItemResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.UUID;

@Tag(name = "favorite items")
@ApiResponse(responseCode = "400", description = "Invalid data provided to the server",
        content = @Content(mediaType = "application/json"))
public interface FavoritesApi {

    @Operation(operationId = "createFavorites", summary = "Добавление продукта во вкладку избранное клиента")
    @ApiResponse(responseCode = "201", description = "CREATED",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UUID.class)))
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #userId,
                T(com.yandex.market.auth.model.Role).USER,
                T(com.yandex.market.auth.util.ClientAttributes).USER_ID
            )
            """
    )
    UUID createFavorites(@Parameter(name = "userId", description = "Идентификатор клиента") UUID userId,
                         @Parameter(name = "productId", description = "Идентификатор продукта") UUID productId);

    @Operation(operationId = "getFavorites", summary = "Получение избранных продуктов клиента")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = FavoriteItemResponseDto.class))))
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #userId,
                T(com.yandex.market.auth.model.Role).USER,
                T(com.yandex.market.auth.util.ClientAttributes).USER_ID
            )
            """
    )
    Page<FavoriteItemResponseDto> getFavorites(
            @Parameter(name = "userId", description = "Идентификатор клиента") UUID userId, Pageable page);

    @DeleteMapping("/users/{userId}/favorites/{productId}")
    @Operation(operationId = "deleteFavorites", summary = "Удаление продукта с избранных клиента")
    @ApiResponse(responseCode = "204", description = "NO_CONTENT")
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #userId,
                T(com.yandex.market.auth.model.Role).USER,
                T(com.yandex.market.auth.util.ClientAttributes).USER_ID
            )
            """
    )
    void deleteFavorites(@Parameter(name = "userId", description = "Идентификатор клиента") UUID userId,
                         @Parameter(name = "productId", description = "Идентификатор продукта") UUID productId);
}
