package com.yandex.market.basketservice.controller;

import com.yandex.market.basketservice.dto.CountItemsResponse;
import com.yandex.market.basketservice.dto.ItemRequest;
import com.yandex.market.basketservice.dto.ItemResponse;
import com.yandex.market.basketservice.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(PUBLIC_API_V1)
@RequiredArgsConstructor
public class BasketController implements BasketApi {

    private final BasketService basketService;

    @GetMapping(value = "/users/{userId}/basket/products")
    @ResponseStatus(OK)
    public Page<ItemResponse> getAllItemsInsideBasketByUserId(@PathVariable UUID userId,
                                                              @PageableDefault(
                                                                      size = 5,
                                                                      sort = "id",
                                                                      direction = Sort.Direction.DESC
                                                              ) Pageable pageable
    ) {
        return basketService.getAllItemsInsideBasketByUserId(userId, pageable);
    }

    @PatchMapping(
            value = "/users/{userId}/basket/products",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    public CountItemsResponse changeItemCountInBasket(@PathVariable UUID userId,
                                                      @RequestBody ItemRequest request) {
        return basketService.changeItemCountInBasket(userId, request);
    }

    @DeleteMapping(
            value = "/users/{userId}/basket/products",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    public CountItemsResponse deleteItemsList(@PathVariable UUID userId,
                                              @RequestBody List<UUID> itemIds) {
        return basketService.deleteItemsList(userId, itemIds);
    }
}