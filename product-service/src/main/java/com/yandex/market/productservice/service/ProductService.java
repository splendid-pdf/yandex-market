package com.yandex.market.productservice.service;

import com.yandex.market.productservice.dto.ProductImageDto;
import com.yandex.market.productservice.dto.projections.ProductPreview;
import com.yandex.market.productservice.dto.projections.SellerArchivePreview;
import com.yandex.market.productservice.dto.projections.SellerProductPreview;
import com.yandex.market.productservice.dto.request.CreateProductRequest;
import com.yandex.market.productservice.dto.request.ProductCharacteristicRequest;
import com.yandex.market.productservice.dto.request.ProductSpecialPriceRequest;
import com.yandex.market.productservice.dto.request.ProductUpdateRequest;
import com.yandex.market.productservice.dto.response.ProductCharacteristicResponse;
import com.yandex.market.productservice.dto.response.ProductResponse;
import com.yandex.market.productservice.dto.response.ProductSpecialPriceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProductService {
    UUID createProduct(CreateProductRequest createProductRequest, UUID sellerId);

    ProductResponse getProductByExternalId(UUID productId);

    ProductResponse updateProductByExternalId(UUID productId, ProductUpdateRequest productUpdateRequest);

    List<ProductResponse> getProductsBySetExternalId(Set<UUID> externalIdSet, Pageable pageable);

    Page<SellerProductPreview> getProductsBySellerId(UUID sellerId, Pageable pageable);

    Page<SellerArchivePreview> getArchivedProductsBySellerId(UUID sellerId, Pageable pageable);

    void moveProductsFromAndToArchive(UUID sellerId, boolean isArchive, List<UUID> productIds);

    void changeProductVisibility(UUID sellerId, boolean isVisible, List<UUID> productIds);

    void deleteProductsBySellerId(UUID sellerId, List<UUID> productIds);

    Page<ProductPreview> getProductPreviews(Pageable pageable);

    List<ProductPreview> getProductPreviewsByIds(Set<UUID> productIdentifiers);

    void changeProductPrice(UUID sellerId, UUID productId, Long updatedPrice);

    void changeProductCount(UUID sellerId, UUID productId, Long updatedCount);

    ProductImageDto addProductImage(UUID productId, ProductImageDto image);

    void deleteProductImage(String imageUrl);

    UUID addProductSpecialPrice(UUID productId, ProductSpecialPriceRequest request);

    ProductSpecialPriceResponse updateSpecialPrice(ProductSpecialPriceRequest request, UUID specialPriceId);

    void deleteProductSpecialPrice(UUID specialPriceId);

    ProductCharacteristicResponse updateProductCharacteristic(UUID characteristicId, ProductCharacteristicRequest request);
}