package com.yandex.market.productservice.dto.response;

import com.yandex.market.productservice.model.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record ProductResponseDto(
         UUID externalId,
         String articleNumber,
         String name,
         String description,
         Long price,
         Long count,
         String articleFromSeller,
         Boolean isVisible,
         Boolean isDeleted,
         LocalDate creationDate,
         TaxType taxType,
         Type type,
         Set<ProductCharacteristic> productCharacteristics,
         Set<ProductImage> productImages,
         Set<ProductSpecialPrice> productSpecialPrices) {
}
