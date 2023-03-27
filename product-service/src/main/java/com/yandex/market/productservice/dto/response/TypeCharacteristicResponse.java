package com.yandex.market.productservice.dto.response;

import com.yandex.market.productservice.model.ValueType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema
public record TypeCharacteristicResponse(
        @NotBlank(message = "Название характеристики должно быть указано")
        @Size(min = 3, max = 30, message = "Название характеристики должно быть в интервале от 3 до 30 символов")
        String name,

        @NotBlank(message = "Значение характеристики должно быть указано")
        @Size(max = 100, message = "Значение характеристики должно быть до 100 символов")
        ValueType valueType
) {
}