package com.yandex.market.favoritesservice.controller;

import com.yandex.market.favoritesservice.controller.api.FavoriteProductApi;
import com.yandex.market.favoritesservice.controller.api.FavoriteSellerApi;
import com.yandex.market.favoritesservice.dto.response.FavoritePreview;
import com.yandex.market.favoritesservice.dto.request.FavoriteProductRequest;
import com.yandex.market.favoritesservice.dto.request.FavoriteSellerRequest;
import com.yandex.market.favoritesservice.service.FavoritesService;
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

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

@Slf4j
@RestController
@RequestMapping(PUBLIC_API_V1)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FavoritesController implements FavoriteProductApi, FavoriteSellerApi {

    private final FavoritesService favoritesService;

    @PostMapping("/users/{userId}/favorites/products")
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createFavoriteProduct(@PathVariable("userId") UUID userId,
                                      @RequestBody @Valid FavoriteProductRequest request) {
        log.info("'createFavoriteProduct' was called for userId = '%s' with request = '%s'".formatted(userId, request));
        return favoritesService.addProductInFavorites(userId, request);
    }

    @PostMapping("/users/{userId}/favorites/sellers")
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createFavoriteSeller(@PathVariable("userId") UUID userId,
                                     @RequestBody @Valid FavoriteSellerRequest request) {
        log.info("'createFavoriteSeller' was called for userId = '%s' with request = '%s'".formatted(userId, request));
        return favoritesService.addSellerInFavorites(userId, request);
    }

    @GetMapping("/users/{userId}/favorites/products")
    @ResponseStatus(HttpStatus.OK)
    public Page<FavoritePreview> getFavoriteProducts(
            @PathVariable("userId") UUID userId,
            @PageableDefault(sort = "addedAt", direction = Sort.Direction.ASC) Pageable page) {
        log.info("'getFavoriteProducts' received a request to get favorites products of user: \"%s\""
                .formatted(userId));
        return favoritesService.getFavoriteProductsByUserId(userId, page);
    }

    @GetMapping("/users/{userId}/favorites/sellers")
    @ResponseStatus(HttpStatus.OK)
    public Page<FavoritePreview> getFavoriteSellers(
            @PathVariable("userId") UUID userId,
            @PageableDefault(sort = "addedAt", direction = Sort.Direction.ASC) Pageable page) {
        log.info("'getFavoriteSellers' received a request to get favorites products of user: \"%s\""
                .formatted(userId));
        return favoritesService.getFavoriteSellersByUserId(userId, page);
    }

    @DeleteMapping("/users/{userId}/favorites/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFavoriteProduct(@PathVariable("userId") UUID userId,
                                      @PathVariable("productId") UUID productId) {
        log.info("'deleteFavoriteProduct' received a request to delete favorite product '%s' of user: '%s'"
                .formatted(productId, userId));
        favoritesService.deleteFavoriteProduct(userId, productId);
    }

    @DeleteMapping("/users/{userId}/favorites/sellers/{sellerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFavoriteSeller(@PathVariable("userId") UUID userId,
                                     @PathVariable("sellerId") UUID sellerId) {
        log.info("'deleteFavoriteSeller' received a request to delete favorite brand '%s' of user: '%s'"
                .formatted(sellerId, userId));
        favoritesService.deleteFavoriteSeller(userId, sellerId);
    }
}