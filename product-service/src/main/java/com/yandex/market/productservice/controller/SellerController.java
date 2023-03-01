package com.yandex.market.productservice.controller;

import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.model.DisplayProductMethod;
import com.yandex.market.productservice.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.app.seller.url}")
@Tag(name = "API for working with the Product entity for Seller")
public class SellerController {

    private final SellerService sellerService;

    @GetMapping("{sellerId}/products")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getProductList",
            summary = "Get Page list for products by sellerId",
            description = "Returns a page of Product (List or archive)")
    @ApiResponse(responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class))))
    public Page<ProductResponseDto> findPageProductsBySellerId(
            @PathVariable UUID sellerId,
            @RequestParam DisplayProductMethod method,
            @PageableDefault(size = 20, sort = "creationDate", direction = Sort.Direction.DESC)
            Pageable pageable) {

        if (method == DisplayProductMethod.PRODUCT_LIST) {
            log.info("Received a request to get Page list for products by sellerId = {}", sellerId);
            return sellerService.getPageOfProductsBySellerId(sellerId, pageable);
        } else if (method == DisplayProductMethod.ARCHIVE) {
            log.info("Received a request to get Page list for products from archive by sellerId = {}", sellerId);
            return sellerService.getArchivePageOfProductsBySellerId(sellerId, pageable);
        } else {
            log.info("Undefined method = {}", method);
            return new PageImpl<>(List.of());
        }
    }
}
