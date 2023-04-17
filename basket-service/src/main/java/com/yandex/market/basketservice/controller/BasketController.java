package com.yandex.market.basketservice.controller;

import com.yandex.market.basketservice.dto.ProductResponseDto;
import com.yandex.market.basketservice.dto.ProductRequestDto;
import com.yandex.market.basketservice.service.BasketService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

    @GetMapping("/{userId}/basket/products")
    @ResponseStatus(OK)
    public Page<ProductResponseDto> getAllProducts(@PathVariable UUID userId,
                                                   @PageableDefault(size = 10,sort = "id", direction = Sort.Direction.DESC)Pageable pageable) {
        return basketService.getAllProducts(userId, pageable);
    }

    @SneakyThrows
    @PostMapping("/{userId}/basket/product")
    @ResponseStatus(OK)
    public Integer changeNumberOfProductsInBasket(@PathVariable UUID userId,
                                                  @RequestBody ProductRequestDto productRequestDto) {
        return basketService.changeNumberOfProductsInBasket(userId, productRequestDto);
    }

    @DeleteMapping("/{userId}/basket/products")
    @ResponseStatus(NO_CONTENT)
    public void deleteProductsSet(@PathVariable UUID userId,
                                      @RequestParam Set<UUID> productIdsSet){
        basketService.deleteProductsSet(userId, productIdsSet);
    }

//    @GetMapping("/{userId}/basket")
//    @ResponseStatus(OK)
//    public BasketResponseDto getBasket(@PathVariable UUID userId) {
//        return basketService.getBasket(userId);
//    }
}
