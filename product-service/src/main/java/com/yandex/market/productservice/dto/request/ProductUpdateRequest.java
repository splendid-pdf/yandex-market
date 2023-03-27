package com.yandex.market.productservice.dto.request;

import com.yandex.market.productservice.model.TaxType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record ProductUpdateRequest(

        String name,

        String description,

        String articleFromSeller,

        Long price,

        Long count,

        TaxType tax,

        boolean isArchived,

        boolean isVisible

) {
}
