package com.yandex.market.productservice.controller;

import com.yandex.market.productservice.controller.response.ErrorResponse;
import com.yandex.market.productservice.dto.ProductImageDto;
import com.yandex.market.productservice.dto.request.CreateProductRequest;
import com.yandex.market.productservice.dto.request.ProductCharacteristicRequest;
import com.yandex.market.productservice.dto.request.ProductSpecialPriceRequest;
import com.yandex.market.productservice.dto.request.ProductUpdateRequest;
import com.yandex.market.productservice.dto.response.ProductCharacteristicResponse;
import com.yandex.market.productservice.dto.response.ProductResponse;
import com.yandex.market.productservice.dto.response.ProductSpecialPriceResponse;
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

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(PUBLIC_API_V1)
@Tag(name = "Product")
@ApiResponses({
        @ApiResponse(
                responseCode = "400",
                description = "На сервер переданы неверные данные",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Продукт не найден",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        )
})
public class ProductController {
    private final ProductService productService;

    //todo: кэширование

    @PostMapping("/sellers/{sellerId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(operationId = "createProduct", summary = "Создание нового продукта")
    @ApiResponse(
            responseCode = "201",
            description = "Продукт успешно создан",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UUID.class)
            )
    )
    public UUID createProduct(@Parameter(name = "productRequestDto", description = "Представление созданного продукта")
                              @RequestBody @Valid CreateProductRequest createProductRequest,
                              @Parameter(name = "sellerId", description = "Идентификатор продавца")
                              @PathVariable("sellerId") UUID sellerId) {
        return productService.createProduct(createProductRequest, sellerId);
    }

    @GetMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getProductById", summary = "Получение товара по id")
    @ApiResponse(
            responseCode = "200",
            description = "Продукт успешно получен",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponse.class)
            )
    )
    public ProductResponse getProductByExternalId(@PathVariable("productId") UUID productId) {
        return productService.getProductByExternalId(productId);
    }

    @PutMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "updateProductById", summary = "Обновление товара по id")
    @ApiResponse(
            responseCode = "200",
            description = "Продукт успешно обновлен",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponse.class)
            )
    )
    public ProductResponse updateProductByExternalId(@PathVariable UUID productId,
                                                     @RequestBody @Valid ProductUpdateRequest productUpdateRequest) {
        return productService.updateProductByExternalId(productId, productUpdateRequest);
    }

    @PostMapping("/products/{productId}/images")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(operationId = "addImage", summary = "Добавить изображение продукту")
    @ApiResponse(
            responseCode = "201",
            description = "Изображение продукта успешно добавлено",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductImageDto.class)
            )
    )
    public ProductImageDto addImage(
            @PathVariable UUID productId,
            @RequestBody ProductImageDto productImageDto
    ) {
        return productService.addProductImage(productId, productImageDto);
    }

    @DeleteMapping("/products/images")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(operationId = "deleteImage", summary = "Удалить изображение продукта")
    @ApiResponse(responseCode = "204", description = "Изображение успешно удалено")
    public void deleteImageByUrl(@RequestParam String url) {
        productService.deleteProductImage(url);
    }

    @DeleteMapping("/products/special-prices/{specialPriceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(operationId = "deleteSpecialPrice", summary = "Удалить акцию")
    @ApiResponse(responseCode = "204", description = "Акция успешно удалена")
    public void deleteSpecialPrice(@PathVariable UUID specialPriceId) {
        productService.deleteProductSpecialPrice(specialPriceId);
    }

    @PostMapping("/products/{productId}/special-prices")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(operationId = "createSpecialPrice", summary = "Добавить акцию")
    @ApiResponse(responseCode = "201", description = "Акция успешно добавлена")
    public UUID createProductSpecialPrice(@RequestBody ProductSpecialPriceRequest productSpecialPriceRequest,
                                          @PathVariable UUID productId
    ) {
        return productService.addProductSpecialPrice(productId, productSpecialPriceRequest);
    }

    @PutMapping("/products/special-prices/{specialPriceId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "createSpecialPrice", summary = "Изменить акцию")
    @ApiResponse(responseCode = "200", description = "Акция успешно изменена")
    public ProductSpecialPriceResponse updateProductSpecialPrice(@RequestBody ProductSpecialPriceRequest productSpecialPriceRequest,
                                                                 @PathVariable UUID specialPriceId
    ) {
        return productService.updateSpecialPrice(productSpecialPriceRequest, specialPriceId);
    }

    @PutMapping("/products/characteristics/{characteristicId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "updateProductCharacteristic", summary = "Обновить характеристику продукта")
    @ApiResponse(responseCode = "200", description = "Характеристика успешно обновлена")
    public ProductCharacteristicResponse updateProductCharacteristic(@PathVariable UUID characteristicId,
                                                                     @RequestBody ProductCharacteristicRequest productCharacteristicRequest
    ) {
        return productService.updateProductCharacteristic(characteristicId, productCharacteristicRequest);
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