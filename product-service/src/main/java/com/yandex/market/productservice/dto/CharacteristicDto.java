package com.yandex.market.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CharacteristicDto(
        @NotBlank(message = "Название характеристики должно быть указано")
        @Size(min = 3, max = 30, message = "Название характеристики должно быть в интервале от 3 до 30 символов")
        String name,

        @NotBlank(message = "Значение характеристики должно быть указано")
        @Size(max = 100, message = "Значение характеристики должно быть до 100 символов")
        String value
) {

}
