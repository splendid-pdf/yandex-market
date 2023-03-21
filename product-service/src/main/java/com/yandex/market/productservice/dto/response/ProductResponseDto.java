package com.yandex.market.productservice.dto.response;

import com.yandex.market.productservice.dto.ProductCharacteristicDto;
import com.yandex.market.productservice.dto.ProductImageDto;
import com.yandex.market.productservice.dto.ProductSpecialPriceDto;
import com.yandex.market.productservice.dto.TypeDto;
import com.yandex.market.productservice.model.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record ProductResponseDto(
         UUID externalId,
         UUID articleNumber,
         String name,
         String description,
         Long price,
         Long count,
         String articleFromSeller,
         Boolean isVisible,
         Boolean isDeleted,
         LocalDate creationDate,
         TaxType taxType,
         TypeDto type,
         Set<ProductCharacteristicDto> productCharacteristics,
         Set<ProductImageDto> productImages,
         Set<ProductSpecialPriceDto> productSpecialPrices) {
}
