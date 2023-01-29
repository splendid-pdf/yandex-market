package com.yandex.market.productservice.dto;

import java.util.List;
import java.util.UUID;

public record ProductResponseDto (
        UUID externalId,
        String name,
        String description,
        ProductType productType,
        String manufacturer,
        String imageUrl,
        Double weight,
        List<CharacteristicDto> characteristics,
        Dimmensions dimmensions,
        Boolean isVisible,
        Double rating
)


{}
