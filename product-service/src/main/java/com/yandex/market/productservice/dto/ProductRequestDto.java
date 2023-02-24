package com.yandex.market.productservice.dto;

import com.yandex.market.productservice.model.TaxType;
import jakarta.validation.constraints.*;

import java.util.Set;

public record ProductRequestDto(
        @NotBlank(message = "Наименование товара должно быть указано")
        @Size(min = 3, max = 30, message = "Наименование товара должно быть в интервале от 3 до 30 символов")
        String name,

        @Size(min = 10, max = 200, message = "Описание товара должно быть в интервале от 10 до 200 символов")
        String description,

        @NotBlank(message = "Производитель товара должен быть указан")
        @Size(min = 3, max = 30, message = "Название производителя должно быть в интервале от 3 до 30 символов")
        String manufacturer,

        boolean isVisible,

        boolean isDeleted,

        @NotNull
        TaxType taxType,

        @PositiveOrZero
        Long price,

        @NotBlank
        String articleFromSeller,

        @Positive
        Long count,

        @NotNull
        TypeDto typeDto,

        @NotEmpty
        Set<ProductCharacteristicDto> productCharacteristicDto,

        @NotEmpty
        Set<ProductImageDto> productImageDto,

        @NotEmpty
        Set<ProductSpecialPriceDto> productSpecialPriceDto
) {
}