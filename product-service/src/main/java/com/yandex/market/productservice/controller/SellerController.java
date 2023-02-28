package com.yandex.market.productservice.controller;

import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.app.seller.url}")
public class SellerController {

    private final SellerService sellerService;

    @GetMapping("{sellerId}/products")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductResponseDto> findPageProductsBySellerId(@PathVariable UUID sellerId,
                                                               @PageableDefault(size = 20, sort = "creationDate", direction = Sort.Direction.DESC)
                                                    Pageable pageable) {
        log.info("Received a request to get Page list for products by sellerId = " + sellerId);
        return sellerService.getPageOfProductsBySellerId(sellerId, pageable);
    }

}
