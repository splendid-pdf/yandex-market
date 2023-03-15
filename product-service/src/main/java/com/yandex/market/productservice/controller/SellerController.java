package com.yandex.market.productservice.controller;

import com.yandex.market.productservice.controller.response.ErrorResponse;
import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.model.DisplayProductMethod;
import com.yandex.market.productservice.model.VisibilityMethod;
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
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class))))
    public Page<ProductResponseDto> findPageProductsBySellerId(
            @PathVariable UUID sellerId,
            @RequestParam DisplayProductMethod method,
            @PageableDefault(size = 20, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return productService.getPageListOrArchiveBySellerId(sellerId, method, pageable);
    }

    @PatchMapping("{sellerId}/products")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(operationId = "deleteOrVisibleProductList", summary = "Изменить видимость продукта по ID продавца")
    @ApiResponse(responseCode = "204", description = "Продукт успешно скрыт / добавлен в архив")
    public void changeProductVisibilityForSeller(@PathVariable(value = "sellerId") UUID sellerId,
                                                 @RequestBody List<UUID> productIds,
                                                 @RequestParam VisibilityMethod method,
                                                 @RequestParam boolean methodAction) {
        productService.changeVisibilityForSellerId(sellerId, productIds, method, methodAction);
    }

    @DeleteMapping("{sellerId}/products")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(operationId = "removeProductsFromArchive", summary = "Удаление списка товаров из базы данных")
    @ApiResponse(responseCode = "204", description = "Продукт успешно удалён")
    public void deleteListProductBySellerId(@PathVariable(value = "sellerId") UUID sellerId,
                                            @RequestBody List<UUID> productIds) {
        productService.deleteFromArchiveListProductBySellerId(productIds, sellerId);
    }
}