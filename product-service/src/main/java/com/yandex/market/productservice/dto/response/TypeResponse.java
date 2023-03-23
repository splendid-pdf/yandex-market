package com.yandex.market.productservice.dto.response;

import com.yandex.market.productservice.dto.TypeCharacteristicDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema
public record TypeResponse(
        UUID id,
        String name,
        List<TypeCharacteristicDto> characteristics

) {
}
