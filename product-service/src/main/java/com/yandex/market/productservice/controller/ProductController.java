package com.yandex.market.productservice.controller;

import com.yandex.market.productservice.controller.response.ErrorResponse;
import com.yandex.market.productservice.dto.ProductRequestDto;
import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.app.product.url}")
@Tag(name = "API для работы с сущностью Product")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "На сервер переданы неверные данные",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Продукт не найден",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)))})
public class ProductController {
    private final ProductService productService;

    @PostMapping("/{sellerId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(operationId = "createProduct", summary = "Создание нового продукта")
    @ApiResponse(responseCode = "201", description = "Продукт успешно создан",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UUID.class)))
    public UUID createProduct(@Parameter(name = "productRequestDto", description = "Представление созданного продукта")
                              @RequestBody @Valid ProductRequestDto productRequestDto,
                              @Parameter(name = "sellerId", description = "Идентификатор продавца")
                              @PathVariable("sellerId") UUID sellerId) {
        return productService.createProduct(productRequestDto, sellerId);
    }

    @GetMapping("{externalId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getProductByExternalId", summary = "Получение товара по externalId")
    @ApiResponse(responseCode = "200", description = "Продукт успешно получен",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class)))
    public ProductResponseDto getProductByExternalId(@PathVariable("externalId") UUID externalId,
                                                     @RequestHeader(value = "X-User-Id", required = false) String userId) {
        return productService.getProductByExternalId(externalId, userId);
    }

    @PutMapping("{externalId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "updateProductByExternalId", summary = "Обновление товара по externalId с входным DTO")
    @ApiResponse(responseCode = "200", description = "Продукт успешно обновлен",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class)))
    public ProductResponseDto updateProductByExternalId(@PathVariable UUID externalId,
                                                        @RequestBody @Valid ProductRequestDto productRequestDto) {
        return productService.updateProductByExternalId(externalId, productRequestDto);
    }

/*    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getProductsBySetExternalId", summary = "Получения списка товаров по externalId каждого товара")
    @ApiResponse(responseCode = "200", description = "Продукт успешно обновлен",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class)))
    public List<ProductResponseDto> getProductsBySetExternalId(
            @RequestParam(name = "extId") Set<UUID> externalIdSet,
            @PageableDefault(sort = {"name"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return productService.getProductsBySetExternalId(externalIdSet, pageable);
    }*/
}