package com.yandex.market.productservice.controller;

import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.model.DisplayProductMethod;
import com.yandex.market.productservice.model.VisibleMethod;
import com.yandex.market.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
@Tag(name = "API для работы с сущностью Product для Seller")
public class SellerController {

    private final ProductService sellerService;

    @GetMapping("{sellerId}/products")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getProductPage", summary = "Получить список продуктов в виде пагинации по ID продавца")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class))))
    public Page<ProductResponseDto> findPageProductsBySellerId(
            @PathVariable UUID sellerId,
            @RequestParam DisplayProductMethod method,
            @PageableDefault(size = 20, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Received a request to get Page list or Archive list for products by sellerId = {}", sellerId);
        return sellerService.getPageListOrArchiveBySellerId(sellerId, method, pageable);
    }

    @PatchMapping("{sellerId}/products")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "deleteOrVisibleProductList", summary = "Изменить видимость продукта по ID продавца")
    @ApiResponse(responseCode = "204", description = "No Content",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class))))
    public void changeProductVisibilityForSeller(@PathVariable(value = "sellerId") UUID sellerId,
                                                 @RequestBody List<UUID> productIds,
                                                 @RequestParam VisibleMethod method,
                                                 @RequestParam boolean methodAction) {
        log.info("A request was received  to change visibility (remove/visibility) for a specific seller with sellerId: {}"
                + " and a list of goods in the number of {} entries.", sellerId, productIds.size());
        sellerService.changeVisibilityForSellerId(sellerId, productIds, method, methodAction);

    }

    @DeleteMapping("{sellerId}/products")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "removeProductsFromArchive", summary = "Удаление списка товаров из базы данных")
    @ApiResponse(responseCode = "204", description = "No Content",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class))))
    public void deleteListProductBySellerId(@PathVariable(value = "sellerId") UUID sellerId,
                                            @RequestBody List<UUID> productIds) {
        log.info("Request for the complete removal of the product(s) in the amount of {} pieces " +
                "for the seller with externalId = {}", productIds.size(), sellerId);
        sellerService.deleteFromArchiveListProductBySellerId(productIds, sellerId);
    }
}