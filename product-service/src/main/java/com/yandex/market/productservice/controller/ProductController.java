package com.yandex.market.productservice.controller;

import com.yandex.market.productservice.dto.ProductRequestDto;
import com.yandex.market.productservice.dto.ProductResponseDto;
import com.yandex.market.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("${spring.controller.url}")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createProduct(@RequestBody ProductRequestDto productRequestDto) {
        log.info("Received request to create a product by given value: {}", productRequestDto);
        return productService.createProduct(productRequestDto);
    }

    @GetMapping("{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto getProductByExternalId(@PathVariable UUID externalId) {
        log.info("Received request to get a product by given value: {}", externalId);
        return productService.getProductByExternalId(externalId);
    }

    @PutMapping("{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto updateProductByExternalId(@PathVariable UUID externalId,
                                                        @RequestBody @Valid ProductRequestDto productRequestDto) {
        log.info("Received request to update a product:{} by given value: {}", externalId, productRequestDto);
        return productService.updateProductByExternalId(externalId, productRequestDto);
    }

    @DeleteMapping("{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProductByExternalId(@PathVariable UUID externalId) {
        log.info("Received request to get a product by given value: {}", externalId);
        productService.deleteProductByExternalId(externalId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDto> getProductsByListUUID(@RequestBody Set<UUID> productIdentifiers) {
        log.info("Received request to get a product by given value: {}", productIdentifiers);
        return productService.getProductsByListUUID(productIdentifiers);
    }

}
