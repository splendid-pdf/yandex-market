package com.yandex.market.productservice.controller;

import com.yandex.market.productservice.controller.response.ErrorResponse;
import com.yandex.market.productservice.dto.ProductImageDto;
import com.yandex.market.productservice.dto.ProductSpecialPriceDto;
import com.yandex.market.productservice.dto.ProductUpdateRequestDto;
import com.yandex.market.productservice.dto.request.CreateProductRequest;
import com.yandex.market.productservice.dto.request.ProductCharacteristicUpdateDto;
import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.dto.response.TypeResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{sellerId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(operationId = "createProduct", summary = "Создание нового продукта")
    @ApiResponse(responseCode = "201", description = "Продукт успешно создан",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UUID.class)))
    public UUID createProduct(@Parameter(name = "productRequestDto", description = "Представление созданного продукта")
                              @RequestBody @Valid CreateProductRequest createProductRequest,
                              @Parameter(name = "sellerId", description = "Идентификатор продавца")
                              @PathVariable("sellerId") UUID sellerId) {
        return productService.createProduct(createProductRequest, sellerId);
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
                                                        @RequestBody @Valid ProductUpdateRequestDto productUpdateRequestDto) {
        return productService.updateProductByExternalId(externalId, productUpdateRequestDto);
    }

    @PostMapping("images/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "addImage", summary = "Добавить изображение продукту")
    @ApiResponse(responseCode = "204", description = "Изображение продукта успешно добавлено",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductImageDto.class)))
    public ProductImageDto addImage(
                                                   @PathVariable UUID productId,
                                                   @RequestBody ProductImageDto productImageDto
    ) {
        return productService.addProductImage(productId, productImageDto);
    }

    @DeleteMapping("images")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "deleteImage", summary = "Удалить изображение продукта")
    @ApiResponse(responseCode = "200", description = "Изображение успешно удалено")
    public void deleteImageByUrl(@RequestParam String url) {
         productService.deleteProductImage(url);
    }

    @DeleteMapping("special-prices/{specialPriceId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "deleteSpecialPrice", summary = "Удалить акцию")
    @ApiResponse(responseCode = "200", description = "Акция успешно удалена")
    public void deleteSpecialPrice(@PathVariable UUID specialPriceId) {
        productService.deleteProductSpecialPrice(specialPriceId);
    }

    @PostMapping("special-prices/{productExternalId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "createSpecialPrice", summary = "Добавить акцию")
    @ApiResponse(responseCode = "204", description = "Акция успешно добавлена")
    public void createProductSpecialPrice(@RequestBody ProductSpecialPriceDto productSpecialPriceDto,
                                          @PathVariable UUID productExternalId
    ) {
        productService.addProductSpecialPrice(productExternalId, productSpecialPriceDto);
    }

    @PutMapping("special-prices/{specialPriceExternalId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "createSpecialPrice", summary = "Изменить акцию")
    @ApiResponse(responseCode = "204", description = "Акция успешно изменена")
    public ProductSpecialPriceDto updateProductSpecialPrice(@RequestBody ProductSpecialPriceDto productSpecialPriceDto,
                                          @PathVariable UUID specialPriceExternalId
    ) {
        return productService.updateSpecialPrice(productSpecialPriceDto, specialPriceExternalId);
    }

    @PutMapping("characteristics/{characteristicExternalId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "updateProductCharacteristic", summary = "Обновить характеристику продукта")
    @ApiResponse(responseCode = "200", description = "Характеристика успешно обновлена")
    public void updateProductSpecialPrice(@PathVariable UUID characteristicExternalId,
                                          @RequestBody ProductCharacteristicUpdateDto productCharacteristicUpdateDto
    ) {
        productService.updateProductCharacteristic(characteristicExternalId, productCharacteristicUpdateDto);
    }

    @GetMapping("types/{typeId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getType", summary = "Получить информацию о типах по id")
    @ApiResponse(responseCode = "200", description = "Информация успешно получена")
    public TypeResponse getTypeById(@PathVariable @Parameter(description = "Идентификатор типа") UUID typeId) {
        return productService.getTypeById(typeId);
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