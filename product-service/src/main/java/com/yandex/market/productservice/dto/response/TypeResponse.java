package com.yandex.market.productservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema
public record TypeResponse(
        UUID id,
        String name,
        List<TypeCharacteristicResponse> characteristics

) {
}
