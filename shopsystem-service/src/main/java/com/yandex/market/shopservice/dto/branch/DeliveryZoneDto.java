package com.yandex.market.shopservice.dto.branch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.Objects;

@Builder
public record DeliveryZoneDto(
        @NotBlank(message = "\"Zone ID\" field must not be empty")
        //use equals&hashcode by this field
        String zoneId,

        @Positive(message = "\"Radius\" field can not be negative")
        int radiusInMeters,

        @Positive(message = "\"Standard delivery price\" field can not be negative")
        double standardDeliveryPrice,

        @Positive(message = "\"Express delivery price\" field can not be negative")
        double expressDeliveryPrice) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryZoneDto that = (DeliveryZoneDto) o;
        return Objects.equals(zoneId, that.zoneId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zoneId);
    }
}
