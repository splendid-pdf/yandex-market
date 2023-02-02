package com.yandex.market.productservice.dto;

import jakarta.validation.constraints.NotBlank;

public record DimensionsDto(
        @NotBlank(message = "Длинна не может быть пустой")
        String length,
        @NotBlank(message = "Ширина не может быть пустой")
        String width
) {
}
