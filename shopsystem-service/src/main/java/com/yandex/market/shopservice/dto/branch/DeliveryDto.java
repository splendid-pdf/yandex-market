package com.yandex.market.shopservice.dto.branch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yandex.market.shopservice.model.branch.PickupPointPartner;
import jakarta.validation.Valid;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {

//    @NotNull(message = "\"UUID of Branch\" field must not be empty")
    private UUID branch;

    private boolean hasDelivery;

    private boolean hasExpressDelivery;

    private boolean hasDeliveryToPickupPoint;

    @JsonIgnoreProperties(value = {"delivery"})
    private Set<PickupPointPartner> pickupPointPartners = new HashSet<>();

    @JsonIgnoreProperties(value = {"delivery"})
    private Set<@Valid DeliveryZoneDto> deliveryZones = new HashSet<>();

    @JsonIgnoreProperties(value = {"delivery"})
    private Set<@Valid DeliveryIntervalDto> deliveryIntervals = new HashSet<>();


}
