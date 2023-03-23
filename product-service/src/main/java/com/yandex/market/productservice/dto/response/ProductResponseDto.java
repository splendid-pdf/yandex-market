package com.yandex.market.productservice.dto.response;

import com.yandex.market.productservice.dto.ProductCharacteristicDto;
import com.yandex.market.productservice.dto.ProductImageDto;
import com.yandex.market.productservice.dto.ProductSpecialPriceDto;
import com.yandex.market.productservice.dto.TypeDto;
import com.yandex.market.productservice.model.TaxType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Schema
public record ProductResponseDto(
         UUID externalId,
         UUID sellerId,
         UUID articleNumber,
         String name,
         String description,
         Long price,
         Long count,
         String articleFromSeller,
         Boolean isVisible,
         Boolean isArchived,
         LocalDateTime creationDate,
         TaxType taxType,
         TypeDto type,
         Set<ProductCharacteristicDto> characteristics,
         Set<ProductImageDto> images,
         Set<ProductSpecialPriceDto> specialPrices) {
}
