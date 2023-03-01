package com.yandex.market.productservice.service;

import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.model.VisibleMethod;
import com.yandex.market.productservice.model.DisplayProductMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface SellerService {

    Page<ProductResponseDto> getPageListOrArchiveBySellerId(UUID sellerId, DisplayProductMethod method, Pageable pageable);

    void changeVisibilityForSellerId(UUID sellerId, List<UUID> productIds, VisibleMethod method, boolean methodAction);

    void deleteFromArchiveListProductBySellerId(List<UUID> productIds, UUID sellerId);
}
