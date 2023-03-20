package com.yandex.market.productservice.service;

import com.yandex.market.productservice.dto.ProductRequestDto;
import com.yandex.market.productservice.dto.response.ProductPreview;
import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.model.DisplayProductMethod;
import com.yandex.market.productservice.model.VisibilityMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProductService {
    UUID createProduct(ProductRequestDto productRequestDto, UUID sellerExternalId);

    ProductResponseDto getProductByExternalId(UUID externalId);

    ProductResponseDto updateProductByExternalId(UUID externalId, ProductRequestDto productRequestDto);

    List<ProductResponseDto> getProductsBySetExternalId(Set<UUID> externalIdSet, Pageable pageable);

    Page<ProductResponseDto> getPageListOrArchiveBySellerId(UUID sellerId, DisplayProductMethod method, Pageable pageable);

    void changeVisibilityForSellerId(UUID sellerId, List<UUID> productIds, VisibilityMethod method, boolean methodAction);

    void deleteFromArchiveListProductBySellerId(List<UUID> productIds, UUID sellerId);

    Page<ProductPreview> getProductPreviews(Pageable pageable);

    List<ProductPreview> getProductPreviewsByIdentifiers(Set<UUID> productIdentifiers);
}