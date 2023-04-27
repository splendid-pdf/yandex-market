package com.yandex.market.favoritesservice.controller.api;

import com.yandex.market.favoritesservice.dto.request.FavoriteProductRequest;
import com.yandex.market.favoritesservice.dto.response.FavoritePreview;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;

public interface FavoriteProductApi extends FavoritesApi {

    @Operation(operationId = "createFavoriteProduct", summary = "Добавление продукта во вкладку избранное клиента")
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
    UUID createFavoriteProduct(@Parameter(name = "userId", description = "Идентификатор клиента") UUID userId,
                               @Schema(name = "request", description = "Идентификатор продукта для добавления в избранное")
                               FavoriteProductRequest request);

    @Operation(operationId = "getFavoriteProducts", summary = "Получение избранных продуктов клиента")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = FavoriteProductRequest.class))))
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #userId,
                T(com.yandex.market.auth.model.Role).USER,
                T(com.yandex.market.auth.util.ClientAttributes).USER_ID
            )
            """
    )
    Page<FavoritePreview> getFavoriteProducts(
            @Parameter(name = "userId", description = "Идентификатор клиента") UUID userId,
            @Parameter(name = "page", description = "Список избранных товаров клиента") Pageable page);

    @Operation(operationId = "deleteFavoriteProduct", summary = "Удаление продукта с избранных клиента")
    @ApiResponse(responseCode = "204", description = "NO_CONTENT")
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #userId,
                T(com.yandex.market.auth.model.Role).USER,
                T(com.yandex.market.auth.util.ClientAttributes).USER_ID
            )
            """
    )
    void deleteFavoriteProduct(@Parameter(name = "userId", description = "Идентификатор клиента") UUID userId,
                               @Parameter(name = "productId", description = "Идентификатор продукта") UUID productId);
}