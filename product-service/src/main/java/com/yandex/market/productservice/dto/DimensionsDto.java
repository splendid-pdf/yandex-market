package com.yandex.market.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DimensionsDto(
        @NotBlank(message = "Длинна не может быть пустой")
        @Pattern(regexp = "\\d+((,|\\.)\\d{2})? [a-zа-я]{1,3}")
        String length,

        @NotBlank(message = "Ширина не может быть пустой")
        @Pattern(regexp = "\\d+((,|\\.)\\d{2})? [a-zа-я]{1,3}")
        String width,

        @Pattern(regexp = "\\d+((,|\\.)\\d{2})? [a-zа-я]{1,3}")
        String height
) {
}
