package com.yandex.market.productservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema
public record SpecialPriceResponse(

        UUID id,
        LocalDateTime fromDate,
        LocalDateTime toDate,
        Long price

) {
}
