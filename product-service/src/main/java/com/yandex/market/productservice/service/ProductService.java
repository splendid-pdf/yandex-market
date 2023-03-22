package com.yandex.market.productservice.service;

import com.yandex.market.productservice.dto.ProductImageDto;
import com.yandex.market.productservice.dto.ProductSpecialPriceDto;
import com.yandex.market.productservice.dto.ProductUpdateRequestDto;
import com.yandex.market.productservice.dto.request.ProductCharacteristicUpdateDto;
import com.yandex.market.productservice.dto.request.CreateProductRequest;
import com.yandex.market.productservice.dto.projections.SellerProductsPreview;
import com.yandex.market.productservice.dto.projections.ProductPreview;
import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.model.DisplayProductMethod;
import com.yandex.market.productservice.model.VisibilityMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProductService {
    UUID createProduct(CreateProductRequest createProductRequest, UUID sellerExternalId);

    ProductResponseDto getProductByExternalId(UUID externalId, String userId);

    ProductResponseDto updateProductByExternalId(UUID externalId, ProductUpdateRequestDto productUpdateRequestDto);

    List<ProductResponseDto> getProductsBySetExternalId(Set<UUID> externalIdSet, Pageable pageable);

    Page<SellerProductsPreview> getPageListOrArchiveBySellerId(UUID sellerId, DisplayProductMethod method, Pageable pageable);

    void changeVisibilityForSellerId(UUID sellerId, List<UUID> productIds, VisibilityMethod method, boolean methodAction);

    void deleteFromArchiveListProductBySellerId(List<UUID> productIds, UUID sellerId);

    Page<ProductPreview> getProductPreviews(Pageable pageable);

    List<ProductPreview> getProductPreviewsByIdentifiers(Set<UUID> productIdentifiers);

    ProductImageDto addProductImage(UUID productId, ProductImageDto productImageDto);

    void deleteProductImage(String url);

    UUID addProductSpecialPrice(UUID productId, ProductSpecialPriceDto productSpecialPriceDto);

    ProductSpecialPriceDto updateSpecialPrice(ProductSpecialPriceDto productSpecialPriceDto,
                                              UUID specialPriceId);

    void deleteProductSpecialPrice(UUID specialPriceId);

    ProductCharacteristicUpdateDto updateProductCharacteristic(UUID productCharacteristicExternalId,
                                                               ProductCharacteristicUpdateDto productCharacteristicUpdateDto);



}