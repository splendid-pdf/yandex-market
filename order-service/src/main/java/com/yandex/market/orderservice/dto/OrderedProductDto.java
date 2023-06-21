package com.yandex.market.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema
public record OrderedProductDto(
        @NotNull(message = "Идентификатор продукта должен быть заполнен")
        UUID productId,
        @Positive(message = "Количество заказанного товара не может быть отрицательным")
        int amount,
//        UUID sellerId,
        String articleFromSeller,
        @NotBlank(message = "Наименование товара должна быть заполнено")
        @Size(min = 1, max = 200, message = "Длина названия товара не может быть меньше 1 и больше 200")
        String name,
        String description,
        @Positive(message = "Цена товара не может быть отрицательной")
        Long price,
        String photoUrl
) {
}