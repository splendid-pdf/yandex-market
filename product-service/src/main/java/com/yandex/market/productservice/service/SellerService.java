package com.yandex.market.productservice.service;

import com.yandex.market.productservice.dto.response.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SellerService {
    Page<ProductResponseDto> getPageOfProductsBySellerId (UUID sellerId, Pageable pageable);

    Page<ProductResponseDto> getArchivePageOfProductsBySellerId(UUID sellerId, Pageable pageable);
}
