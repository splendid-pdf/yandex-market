package com.yandex.market.productservice.dto.request;

import com.yandex.market.productservice.dto.*;
import com.yandex.market.productservice.model.TaxType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.Set;

@Schema
public record CreateProductRequest(
        @NotBlank(message = "Наименование товара должно быть указано")
        @Size(min = 3, max = 30, message = "Наименование товара должно быть в интервале от 3 до 30 символов")
        String name,

        @Size(min = 10, max = 200, message = "Описание товара должно быть в интервале от 10 до 200 символов")
        String description,

        @NotBlank
        String articleFromSeller,

        @PositiveOrZero
        Long price,

        @Positive
        Long count,

        @NotNull
        TypeDto type,

        @NotEmpty
        Set<@Valid ProductCharacteristicDto> characteristics,

        @NotNull
        TaxType taxType,

        @NotEmpty
        Set<@Valid ProductImageDto> images,

        Set<@Valid ProductSpecialPriceRequest> specialPrices
) {
}