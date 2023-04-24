package com.yandex.market.favoritesservice.controller;

import com.yandex.market.favoritesservice.dto.FavoriteItemResponseDto;
import com.yandex.market.favoritesservice.service.FavoriteItemService;
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
public class FavoritesController implements FavoritesApi {

    private final FavoriteItemService favoriteItemService;

    @PostMapping("/users/{userId}/favorites/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createFavorites(@PathVariable("userId") UUID userId, @PathVariable("productId") UUID productId) {
        log.info("Received a request to added product '%s' in favorites for user '%s'".formatted(productId, userId));
        return favoriteItemService.addItemInFavorites(productId, userId);
    }

    @GetMapping("/users/{userId}/favorites")
    @ResponseStatus(HttpStatus.OK)
    public Page<FavoriteItemResponseDto> getFavorites(
            @PathVariable("userId") UUID userId,
            @PageableDefault(sort = "addedAt", direction = Sort.Direction.ASC) Pageable page) {
        log.info("Received a request to get favorites products of user: \"%s\"".formatted(userId));
        return favoriteItemService.getFavoritesByUserId(userId, page);
    }

    @DeleteMapping("/users/{userId}/favorites/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFavorites(@PathVariable("userId") UUID userId, @PathVariable("productId") UUID productId) {
        log.info("Received a request to delete favorites product \"%s\" of user: \"%s\"".formatted(productId, userId));
        favoriteItemService.deleteFavoritesByUserId(userId, productId);
    }
}