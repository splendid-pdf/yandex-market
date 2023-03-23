package com.yandex.market.productservice.controller;

import com.yandex.market.productservice.controller.response.ErrorResponse;
import com.yandex.market.productservice.dto.projections.SellerArchivePreview;
import com.yandex.market.productservice.dto.projections.SellerProductPreview;

import com.yandex.market.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "На сервер переданы неверные данные",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Продукт не найден",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)))})
public class SellerController {

    private final ProductService productService;

    @GetMapping("{sellerId}/products")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getProductPage", summary = "Метод возращает пагинированный список продуктов продавца")
    @ApiResponse(responseCode = "200", description = "Список продуктов успешно получен",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SellerProductPreview.class))))
    public Page<SellerProductPreview> findProductsBySellerId(
            @PathVariable UUID sellerId,
            @PageableDefault(size = 20, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return productService.getProductsBySellerId(sellerId, pageable);
    }

    @GetMapping("{sellerId}/archived-products")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getArchivedProductPage",
            summary = "Метод возращает пагинированный список продуктов из архива продавца")
    @ApiResponse(responseCode = "200", description = "Архив продуктов успешно получен",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SellerProductPreview.class))))
    public Page<SellerArchivePreview> findArchivedProductsBySellerId(
            @PathVariable UUID sellerId,
            @PageableDefault(size = 20, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return productService.getArchivedProductsBySellerId(sellerId, pageable);
    }

    @PatchMapping("{sellerId}/products/archive")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Добавить продукты в архив от лица продавца")
    @ApiResponse(responseCode = "200", description = "Продукты успешно добавлены в архив")
    public void moveProductsToArchive(@PathVariable(value = "sellerId") UUID sellerId,
                                      @RequestParam("is-archive") boolean isArchive,
                                      @RequestBody List<UUID> productIds) {
        productService.moveProductsFromAndToArchive(sellerId, isArchive, productIds);
    }

    @PatchMapping("{sellerId}/products/visibility")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Сделать продукты видимыми от лица продавца")
    @ApiResponse(responseCode = "200", description = "Продукты успешно стали видимыми")
    public void makeProductsVisible(@PathVariable(value = "sellerId") UUID sellerId,
                                    @RequestParam("is-visible") boolean isVisible,
                                    @RequestBody List<UUID> productIds) {
        productService.changeProductVisibility(sellerId, isVisible, productIds);
    }

    @PatchMapping("{sellerId}/products/{productId}/price")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "changeProductPrice", summary = "Изменить цену продукта")
    @ApiResponse(responseCode = "200", description = "Продукт успешно изменён в цене")
    public void changeProductPrice(@PathVariable(value = "sellerId") UUID sellerId,
                                   @PathVariable(value = "productId") UUID productId,
                                   @RequestParam Long updatedPrice) {
        productService.changeProductPrice(sellerId, productId, updatedPrice);
    }

    @DeleteMapping("{sellerId}/products/deleted")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "deleteProducts", summary = "Удаление списка товаров из базы данных")
    @ApiResponse(responseCode = "200", description = "Продукт успешно удалён")
    public void deleteProducts(@PathVariable(value = "sellerId") UUID sellerId,
                               @RequestBody List<UUID> productIds) {
        productService.deleteProductsBySellerId(sellerId, productIds);
    }
}