package com.marketplace.aggregatorservice.dto;

import java.util.Set;

public record ProductRequestDto(
        String name,

        String description,

        String articleFromSeller,

        Long price,

        Long count,

        TypeDto typeDto,

        Set<ProductCharacteristicDto> productCharacteristicDto,

        String manufacturer,

        TaxType taxType,

        Set<ProductImageDto> productImageDto,

        boolean isVisible,

        boolean isDeleted,

        Set<ProductSpecialPriceDto> productSpecialPriceDto
) {
}