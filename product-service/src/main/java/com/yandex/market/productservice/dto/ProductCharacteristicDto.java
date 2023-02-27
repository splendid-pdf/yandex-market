package com.yandex.market.productservice.dto;

import com.yandex.market.productservice.model.ValueType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema
public record ProductCharacteristicDto(
        @NotBlank
        String name,
        @NotBlank
        String value,
        @NotNull
        ValueType valueType) {
}