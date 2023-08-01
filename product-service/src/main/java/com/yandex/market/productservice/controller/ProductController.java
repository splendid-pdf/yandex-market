package com.yandex.market.productservice.controller;

import com.yandex.market.productservice.dto.ProductCountDto;
import com.yandex.market.productservice.dto.ProductImageDto;
import com.yandex.market.productservice.dto.projections.SellerArchiveProductPreview;
import com.yandex.market.productservice.dto.projections.SellerProductPreview;
import com.yandex.market.productservice.dto.projections.UserProductPreview;
import com.yandex.market.productservice.dto.request.CreateProductRequest;
import com.yandex.market.productservice.dto.request.ProductCharacteristicRequest;
import com.yandex.market.productservice.dto.request.ProductUpdateRequest;
import com.yandex.market.productservice.dto.request.SpecialPriceRequest;
import com.yandex.market.productservice.dto.response.ProductCharacteristicResponse;
import com.yandex.market.productservice.dto.response.ProductCountDtoResponse;
import com.yandex.market.productservice.dto.response.ProductResponse;
import com.yandex.market.productservice.dto.response.SpecialPriceResponse;
import com.yandex.market.productservice.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(PUBLIC_API_V1)
public class ProductController implements ProductApi {
    private final ProductService productService;

    //todo: кэширование
    //todo: id в error response
    //todo: немного рефактора везде(теперь реально немного, убрать хард код где - то импорты)
    //todo: фильтр (поиск по имени и характеристикам) - карточка
    //todo: возможность добавлять новые комнаты и типы только для админов, тянем секьюрити помни про валидатор и мапу там
    //todo: возможность редактировать комнаты и типы только для админов, тянем секьюрити помни про валидатор и мапу там
    //todo: фильтр (поиск по имени и характеристикам) глянь Specification
    //todo: главная страница магазина - карточка (4 свежих)
    //todo: характеристикам завести название группы характеристик. Например у веса ширины глубины
    // это поле будет иметь значение габариты и тд. По хорошему менять базу отношения в базе и заводить новую таблитцу,
    // но как же впадлу. Поэтому ограничемся полем

    //todo мапу из валидатора вынести в отдельный бин и инжектить куда надо чтобы в бд не обращаться лишний раз

    @PostMapping("/sellers/{sellerId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createProduct(@PathVariable UUID sellerId,
                              @RequestBody @Valid CreateProductRequest createProductRequest) {
        return productService.createProduct(createProductRequest, sellerId);
    }

    @GetMapping("/sellers/{sellerId}/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductById(@PathVariable UUID sellerId,
                                          @PathVariable UUID productId) {
        return productService.getProductById(productId);
    }

    @GetMapping("/sellers/{sellerId}/products")
    @ResponseStatus(HttpStatus.OK)
    public Page<SellerProductPreview> getProductPreviewsBySellerId(
            @PathVariable UUID sellerId,
            @PageableDefault(size = 20, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return productService.getProductsBySellerId(sellerId, pageable);
    }

    @GetMapping("/sellers/{sellerId}/archive/products")
    @ResponseStatus(HttpStatus.OK)
    public Page<SellerArchiveProductPreview> getArchivedProductPreviewsBySellerId(
            @PathVariable UUID sellerId,
            @PageableDefault(size = 20, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return productService.getArchivedProductsBySellerId(sellerId, pageable);
    }

    @PutMapping("/sellers/{sellerId}/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProductById(@PathVariable UUID sellerId,
                                             @PathVariable UUID productId,
                                             @RequestBody @Valid ProductUpdateRequest productUpdateRequest) {
        return productService.updateProductById(productId, productUpdateRequest);
    }

    @PatchMapping("/sellers/{sellerId}/archive/products")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeIsArchiveField(@PathVariable UUID sellerId,
                                     @RequestParam("is-archive") boolean isArchive,
                                     @RequestParam("product-ids") List<UUID> productIds) {
        productService.changeIsArchiveField(isArchive, productIds);
    }

    @PatchMapping("/sellers/{sellerId}/products/visibility")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeProductsVisibility(@PathVariable UUID sellerId,
                                         @RequestParam("is-visible") boolean isVisible,
                                         @RequestParam("product-ids") List<UUID> productIds) {
        productService.changeProductsVisibility(isVisible, productIds);
    }

    @DeleteMapping("/sellers/{sellerId}/products")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductsList(@PathVariable UUID sellerId,
                                   @RequestParam("product-ids") List<UUID> productIds) {
        productService.deleteProductsBySellerId(sellerId, productIds);
    }

    @PatchMapping("/sellers/{sellerId}/products/{productId}/price")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeProductPriceById(@PathVariable UUID sellerId,
                                       @PathVariable UUID productId,
                                       @RequestParam(value = "price") @Positive Long updatedPrice) {
        productService.changeProductPrice(productId, updatedPrice);
    }

    @PutMapping("/sellers/{sellerId}/products/{productId}/count")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeProductCountById(@PathVariable UUID sellerId,
                                       @PathVariable UUID productId,
                                       @RequestParam(value = "count") @PositiveOrZero Long updatedCount) {
        productService.changeProductCount(productId, updatedCount);
    }

    @PutMapping("/sellers/{sellerId}/products/counts")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductCountDtoResponse> changeAmountOfProductsByIds(@PathVariable UUID sellerId,
                                                                     @RequestBody List<ProductCountDto> productCountDtoList) {
        return productService.changeAmountOfProductsByIds(sellerId, productCountDtoList);
    }

    @PostMapping("/sellers/{sellerId}/products/{productId}/images")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductImageDto addImage(@PathVariable UUID sellerId,
                                    @PathVariable UUID productId,
                                    @RequestBody @Valid ProductImageDto productImage) {
        return productService.addProductImage(productId, productImage);
    }

    @DeleteMapping("/sellers/{sellerId}/products/images")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImageByUrl(@PathVariable UUID sellerId,
                                 @RequestParam @URL String url) {
        productService.deleteProductImage(url);
    }

    @PostMapping("/sellers/{sellerId}/products/{productId}/special-prices")
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createSpecialPriceByProductId(@PathVariable UUID sellerId,
                                              @PathVariable UUID productId,
                                              @RequestBody @Valid SpecialPriceRequest specialPriceRequest) {
        return productService.addProductSpecialPrice(productId, specialPriceRequest);
    }

    @PutMapping("/sellers/{sellerId}/products/special-prices/{specialPriceId}")
    @ResponseStatus(HttpStatus.OK)
    public SpecialPriceResponse updateSpecialPriceById(@PathVariable UUID sellerId,
                                                       @PathVariable UUID specialPriceId,
                                                       @RequestBody @Valid SpecialPriceRequest specialPriceRequest) {
        return productService.updateSpecialPrice(specialPriceRequest, specialPriceId);
    }

    @DeleteMapping("/sellers/{sellerId}/products/special-prices/{specialPriceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSpecialPriceById(@PathVariable UUID sellerId,
                                       @PathVariable UUID specialPriceId) {
        productService.deleteProductSpecialPrice(specialPriceId);
    }

    @PutMapping("/sellers/{sellerId}/products/characteristics/{characteristicId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductCharacteristicResponse updateProductCharacteristicById(
            @PathVariable UUID sellerId,
            @PathVariable UUID characteristicId,
            @RequestBody @Valid ProductCharacteristicRequest productCharacteristicRequest
    ) {
        return productService.updateProductCharacteristic(characteristicId, productCharacteristicRequest);
    }

    @GetMapping("/product-previews")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserProductPreview> getAllProductPreviews(@PageableDefault Pageable pageable) {
        return productService.getProductPreviews(pageable);
    }

    @PostMapping("/product-previews")
    @ResponseStatus(HttpStatus.OK)
    public List<UserProductPreview> getProductPreviewsByIdentifiers(@RequestParam("product-ids") Set<UUID> productIds) {
        return productService.getProductPreviewsByIds(productIds);
    }
}