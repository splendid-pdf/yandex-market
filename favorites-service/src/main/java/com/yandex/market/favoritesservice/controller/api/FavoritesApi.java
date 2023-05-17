package com.yandex.market.favoritesservice.controller.api;

import com.yandex.market.model.ErrorResponse;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "favorites-service")
@ApiResponses({
        @ApiResponse(
                responseCode = "400",
                description = "Некорректный запрос к серверу",
                content = {
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponse.class)
                        )
                }
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Для доступа к ресурсу требуется аутентификация"
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Доступ к ресурсу запрещен"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Ресурс не найден",
                content = {
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponse.class)
                        )
                }
        )
})
public interface FavoritesApi {
}