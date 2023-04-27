package com.yandex.market.favoritesservice.controller.api;

import com.yandex.market.favoritesservice.dto.request.FavoriteSellerRequest;
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

public interface FavoriteSellerApi extends FavoritesApi {

    @Operation(operationId = "createFavoriteSeller", summary = "Добавление магазина во вкладку избранное клиента")
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
    UUID createFavoriteSeller(@Parameter(name = "userId", description = "Идентификатор клиента") UUID userId,
                              @Schema(name = "request", description = "Идентификатор магазина для добавления в избранное")
                              FavoriteSellerRequest request);

    @Operation(operationId = "getFavoriteSellers", summary = "Получение избранных магазинов клиента")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = FavoriteSellerRequest.class))))
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #userId,
                T(com.yandex.market.auth.model.Role).USER,
                T(com.yandex.market.auth.util.ClientAttributes).USER_ID
            )
            """
    )
    Page<FavoritePreview> getFavoriteSellers(
            @Parameter(name = "userId", description = "Идентификатор клиента") UUID userId,
            @Parameter(name = "page", description = "Список избранных магазинов клиента") Pageable page);

    @Operation(operationId = "deleteFavoriteSeller", summary = "Удаление магазина с избранных клиента")
    @ApiResponse(responseCode = "204", description = "NO_CONTENT")
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #userId,
                T(com.yandex.market.auth.model.Role).USER,
                T(com.yandex.market.auth.util.ClientAttributes).USER_ID
            )
            """
    )
    void deleteFavoriteSeller(@Parameter(name = "userId", description = "Идентификатор клиента") UUID userId,
                               @Parameter(name = "productId", description = "Идентификатор магазина") UUID sellerId);
}
