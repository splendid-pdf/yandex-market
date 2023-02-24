package com.yandex.market.productservice.service;

import com.yandex.market.productservice.dto.response.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface SellerService {
    Page<ProductResponseDto> getPageOfProductsBySellerId(UUID sellerId, Pageable pageable);

    void hideProductListForSeller(List<UUID> productIds, UUID sellerId);

    void displayProductListForSeller(List<UUID> productIds, UUID sellerId);

    void addListOfGoodsToArchiveForSeller(List<UUID> productIds, UUID sellerId);

    void returnListOfGoodsFromArchiveToSeller(List<UUID> productIds, UUID sellerId);
}
