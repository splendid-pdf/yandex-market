package com.yandex.market.productservice.models;

import com.yandex.market.productservice.dto.ProductImageDto;
import com.yandex.market.productservice.dto.response.ProductCharacteristicResponse;
import com.yandex.market.productservice.dto.response.TypePreviewResponse;
import com.yandex.market.productservice.model.TaxType;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        UUID sellerId,
        String name,
        String brand,
        String description,
        Long price,
        Long count,
        String articleFromSeller,
        Boolean isVisible,
        Boolean isArchived,
        LocalDateTime creationDate,
        TaxType tax,
        TypePreviewResponse type,
        Set<ProductCharacteristicResponse> characteristics,
        Set<ProductImageDto> images) {
}