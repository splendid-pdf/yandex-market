package com.yandex.market.shopservice.dto.branch;

import com.yandex.market.shopservice.model.branch.Delivery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record DeliveryZoneDto(
        @NotBlank(message = "\"Zone ID\" field must not be empty")
        String zoneId,

        @Valid
        // @NotNull
        Delivery delivery,

        @Positive(message = "\"Radius\" field can not be negative")
        int radiusInMeters,

        @Positive(message = "\"Standard delivery price\" field can not be negative")
        double standardDeliveryPrice,

        @Positive(message = "\"Express delivery price\" field can not be negative")
        double expressDeliveryPrice) {
}
