package com.yandex.market.shopservice.dto.branch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DeliveryZoneDto {
    @EqualsAndHashCode.Include
    @NotBlank(message = "\"Zone ID\" field must not be empty")
    private String zoneId;

    @Positive(message = "\"Radius\" field can not be negative")
    private int radiusInMeters;

    @Positive(message = "\"Standard delivery price\" field can not be negative")
    private double standardDeliveryPrice;

    @Positive(message = "\"Express delivery price\" field can not be negative")
    private double expressDeliveryPrice;
}