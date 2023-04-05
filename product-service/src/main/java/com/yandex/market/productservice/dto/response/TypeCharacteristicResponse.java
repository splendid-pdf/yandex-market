package com.yandex.market.productservice.dto.response;

import com.yandex.market.productservice.model.ValueType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema
public record TypeCharacteristicResponse(

        String name,
        ValueType valueType

) {
}