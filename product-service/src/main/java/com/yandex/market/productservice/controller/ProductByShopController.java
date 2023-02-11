package com.yandex.market.productservice.controller;

import com.yandex.market.productservice.dto.request.ProductPriceRequestDto;
import com.yandex.market.productservice.dto.response.ProductFullInfoResponse;
import com.yandex.market.productservice.dto.response.ProductPriceResponseDto;
import com.yandex.market.productservice.service.ProductsByShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.app.shops.url}")
public class ProductByShopController {

    private final ProductsByShopService service;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createProductPrice(@RequestBody ProductPriceRequestDto productPrice) {
        log.info("Received a request to create new product: " + productPrice);
        return service.createProductPrice(productPrice);
    }

    @GetMapping("{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductPriceResponseDto getProductsByExternalId(@PathVariable(value = "externalId") UUID externalId) {
        log.info("Received a request to get product price by external ID: " + externalId);
        return service.getProductsByExternalId(externalId);
    }

    @GetMapping("shops/{shopId}/products")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductFullInfoResponse> getProductsByShop(@PathVariable(value = "shopId") UUID shopId,
                                                           @PageableDefault(size = 20) Pageable pageable) {
        log.info("Received a request to get Page list for products by shop ID: " + shopId);
        return service.getProductsByShop(shopId, pageable);
    }

    @GetMapping("branches/{branchId}/products")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductFullInfoResponse> getProductsByBranch(@PathVariable(value = "branchId") UUID branchId,
                                                             @PageableDefault(size = 20) Pageable pageable) {
        log.info("Received a request to get Page list for products by branch ID: " + branchId);
        return service.getProductsByBranch(branchId, pageable);
    }
}
