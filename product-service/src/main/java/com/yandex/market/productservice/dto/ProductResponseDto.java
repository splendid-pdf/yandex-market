package com.yandex.market.productservice.dto;

import com.yandex.market.productservice.model.Dimensions;
import com.yandex.market.productservice.model.ProductType;

import java.util.List;
import java.util.UUID;

public record ProductResponseDto(
        UUID externalId,
        String name,
        String description,
        ProductType productType,
        String manufacturer,
        String imageUrl,
        Double weight,
        List<CharacteristicDto> characteristics,
        Dimensions dimensions,
        Boolean isVisible,
        Double rating
) {
}