package com.yandex.market.productservice.service;

import com.yandex.market.productservice.dto.ProductImageDto;
import com.yandex.market.productservice.dto.ProductSpecialPriceDto;
import com.yandex.market.productservice.dto.ProductUpdateRequestDto;
import com.yandex.market.productservice.dto.projections.SellerArchivePreview;
import com.yandex.market.productservice.dto.request.ProductCharacteristicUpdateDto;
import com.yandex.market.productservice.dto.request.CreateProductRequest;
import com.yandex.market.productservice.dto.projections.SellerProductPreview;
import com.yandex.market.productservice.dto.projections.ProductPreview;
import com.yandex.market.productservice.dto.response.ProductResponseDto;
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

    Page<SellerProductPreview> getProductsBySellerId(UUID sellerId, Pageable pageable);

    Page<SellerArchivePreview> getArchivedProductsBySellerId(UUID sellerId, Pageable pageable);

    void addProductsToArchive(UUID sellerId, List<UUID> productIds);

    void returnProductsFromArchive(UUID sellerId, List<UUID> productIds);

    void makeProductsVisible(UUID sellerId, List<UUID> productIds);
    void makeProductsInvisible(UUID sellerId, List<UUID> productIds);

    void deleteProductsBySellerId(UUID sellerId, List<UUID> productIds);

    Page<ProductPreview> getProductPreviews(Pageable pageable);

    List<ProductPreview> getProductPreviewsByIdentifiers(Set<UUID> productIdentifiers);

    void changeProductPrice(UUID sellerId, UUID productId, Long newPrice);
    
    ProductImageDto addProductImage(UUID productId, ProductImageDto productImageDto);

    void deleteProductImage(String url);

    UUID addProductSpecialPrice(UUID productId, ProductSpecialPriceDto productSpecialPriceDto);

    ProductSpecialPriceDto updateSpecialPrice(ProductSpecialPriceDto productSpecialPriceDto,
                                              UUID specialPriceId);

    void deleteProductSpecialPrice(UUID specialPriceId);

    ProductCharacteristicUpdateDto updateProductCharacteristic(UUID productCharacteristicExternalId,
                                                               ProductCharacteristicUpdateDto productCharacteristicUpdateDto);
}