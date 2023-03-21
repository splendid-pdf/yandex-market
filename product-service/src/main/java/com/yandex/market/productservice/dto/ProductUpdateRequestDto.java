package com.yandex.market.productservice.dto;

import com.yandex.market.productservice.model.TaxType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record ProductUpdateRequestDto (

        String name,

        String description,

        String articleFromSeller,

        Long price,

        Long count,

        TaxType taxType,

        boolean isArchived,

        boolean isDeleted

) {
}
