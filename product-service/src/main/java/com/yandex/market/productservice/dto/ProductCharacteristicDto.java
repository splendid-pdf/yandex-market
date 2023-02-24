package com.yandex.market.productservice.dto;

import com.yandex.market.productservice.model.ValueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductCharacteristicDto(
        @NotBlank
        String name,
        @NotBlank
        String value,
        @NotNull
        ValueType valueType) {
}