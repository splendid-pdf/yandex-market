package com.yandex.market.favoritesservice.controller;

import com.yandex.market.favoritesservice.dto.FavoritesRequestDto;
import com.yandex.market.favoritesservice.dto.FavoritesResponseDto;
import com.yandex.market.favoritesservice.service.FavoritesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@Slf4j
@RestController
@RequestMapping("${spring.application.url}")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "favorites")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Invalid data provided to the server",
                content = @Content(mediaType = "application/json"))})
public class FavoritesController {

    private final FavoritesService favoritesService;

    private static final String USER_ID = "userId";

    @PostMapping("/{userId}/favorites")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(operationId = "createFavorites", summary = "Add product in favorites for user")
    @ApiResponse(responseCode = "201", description = "CREATED",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UUID.class)))
    private UUID createFavorites(
            @Parameter(name = "favoritesRequestDto", description = "Representation of a created favorites")
            @RequestBody @Valid FavoritesRequestDto favoritesRequestDto,
            @Parameter(name = USER_ID, description = "User's identifier")
            @PathVariable(USER_ID) UUID userId) {
        log.info("Received a request to added product \"%s\" in favorites for user \"%s\""
                .formatted(favoritesRequestDto.productId(), userId));
        return favoritesService.createFavorites(favoritesRequestDto, userId);
    }

    @GetMapping("/{userId}/favorites")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getFavorites", summary = "Get favorites product of user")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = FavoritesResponseDto.class))))
    private Page<FavoritesResponseDto> getFavorites(
            @Parameter(name = USER_ID, description = "User's identifier")
            @PathVariable(USER_ID) UUID userId,
            @PageableDefault(sort = "additionTimestamp", direction = Sort.Direction.DESC) Pageable page) {
        log.info("Received a request to get favorites products of user: \"%s\"".formatted(userId));
        return favoritesService.getFavoritesByUserId(userId, page);
    }

    @DeleteMapping("/{userId}/favorites/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(operationId = "deleteFavorites", summary = "Delete favorites product of user")
    @ApiResponse(responseCode = "204", description = "NO_CONTENT")
    private void deleteFavorites(
            @Parameter(name = USER_ID, description = "User's identifier")
            @PathVariable(USER_ID) UUID userId,
            @Parameter(name = "productId", description = "Product's identifier")
            @PathVariable("productId") UUID productId) {
        log.info("Received a request to delete favorites product \"%s\" of user: \"%s\""
                .formatted(productId, userId));
        favoritesService.deleteFavoritesByUserId(userId, productId);
    }
}