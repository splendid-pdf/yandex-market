package com.yandex.market.favoritesservice.controller;

import com.yandex.market.favoritesservice.dto.FavoriteItemResponseDto;
import com.yandex.market.favoritesservice.service.FavoriteItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

@Slf4j
@RestController
@RequestMapping(PUBLIC_API_V1)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "favorite items")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Invalid data provided to the server",
                content = @Content(mediaType = "application/json"))})
public class FavoriteItemController {

    private final FavoriteItemService favoriteItemService;

    @PostMapping("/users/{userId}/favorites/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(operationId = "createFavorites", summary = "Добавление продукта во вкладку избранное клиента")
    @ApiResponse(responseCode = "201", description = "CREATED",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UUID.class)))
    public UUID createFavorites(
            @Parameter(name = "userId", description = "Идентификатор клиента") @PathVariable UUID userId,
            @Parameter(name = "productId", description = "Идентификатор продукта") @PathVariable UUID productId) {
        log.info("Received a request to added product '%s' in favorites for user '%s'".formatted(productId, userId));
        return favoriteItemService.addItemInFavorites(productId, userId);
    }

    @GetMapping("/users/{userId}/favorites")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getFavorites", summary = "Получение избранных продуктов клиента")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = FavoriteItemResponseDto.class))))
    public Page<FavoriteItemResponseDto> getFavorites(
            @Parameter(name = "userId", description = "Идентификатор клиента")
            @PathVariable("userId") UUID userId,
            @PageableDefault(sort = "addedAt", direction = Sort.Direction.DESC) Pageable page) {
        log.info("Received a request to get favorites products of user: \"%s\"".formatted(userId));
        return favoriteItemService.getFavoritesByUserId(userId, page);
    }

    @DeleteMapping("/users/{userId}/favorites/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(operationId = "deleteFavorites", summary = "Удаление продукта с избранных клиента")
    @ApiResponse(responseCode = "204", description = "NO_CONTENT")
    public void deleteFavorites(
            @Parameter(name = "userId", description = "Идентификатор клиента")
            @PathVariable("userId") UUID userId,
            @Parameter(name = "productId", description = "Идентификатор продукта")
            @PathVariable("productId") UUID productId) {
        log.info("Received a request to delete favorites product \"%s\" of user: \"%s\""
                .formatted(productId, userId));
        favoriteItemService.deleteFavoritesByUserId(userId, productId);
    }
}