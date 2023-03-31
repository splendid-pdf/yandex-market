package com.yandex.market.productservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record ProductUpdateRequest(

        String name,

        String brand,

        String description,

        String articleFromSeller,

        Long price,

        Long count,

        boolean isArchived,

        boolean isVisible

) {
}
