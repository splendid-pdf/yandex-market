package com.yandex.market.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema
public record AddressRequest(
        @NotBlank(message = "Адрес доставки должен быть заполнен")
        String deliveryAddress,

        @NotBlank(message = "Номер подъезда должен быть заполнен")
        String entrance,

        @NotBlank(message = "Этаж должен быть заполнен")
        String floor,

        @NotBlank(message = "Номер квартиры должен быть заполнен")
        String apartment,

        String intercom,

        String comment
) {
}