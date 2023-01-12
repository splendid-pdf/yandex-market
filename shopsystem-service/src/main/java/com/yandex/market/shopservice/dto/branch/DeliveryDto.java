package com.yandex.market.shopservice.dto.branch;

import com.yandex.market.shopservice.model.branch.PickupPointPartner;
import jakarta.validation.Valid;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {
    private UUID branch;

    private boolean hasDelivery;

    private boolean hasExpressDelivery;

    private boolean hasDeliveryToPickupPoint;

    private Set<PickupPointPartner> pickupPointPartners;

    private Set<@Valid DeliveryZoneDto> deliveryZones;

    private Set<@Valid DeliveryIntervalDto> deliveryIntervals;
}
