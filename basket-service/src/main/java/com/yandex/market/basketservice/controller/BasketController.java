package com.yandex.market.basketservice.controller;

import com.yandex.market.basketservice.dto.ItemResponse;
import com.yandex.market.basketservice.dto.ItemRequest;
import com.yandex.market.basketservice.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(PUBLIC_API_V1)
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @GetMapping("/users/{userId}/basket/products")
    @ResponseStatus(OK)
    public Page<ItemResponse> getAllItemsInsideBasketByUserId(@PathVariable UUID userId,
                                                              @PageableDefault(
                                                                      size = 10,
                                                                      sort = "id",
                                                                      direction = Sort.Direction.DESC
                                                              ) Pageable pageable
    ) {
        return basketService.getAllItemsInsideBasketByUserId(userId, pageable);
    }

    @PatchMapping("/users/{userId}/basket/product")
    @ResponseStatus(OK)
    public void changeItemCountInBasket(@PathVariable UUID userId,
                                        @RequestBody ItemRequest itemRequest) {
        basketService.changeItemCountInBasket(userId, itemRequest);
    }

    @DeleteMapping("/users/{userId}/basket/products")
    @ResponseStatus(NO_CONTENT)
    public void deleteItemsList(@PathVariable UUID userId,
                                @RequestParam List<UUID> productIdsList) {
        basketService.deleteItemsList(userId, productIdsList);
    }
}
