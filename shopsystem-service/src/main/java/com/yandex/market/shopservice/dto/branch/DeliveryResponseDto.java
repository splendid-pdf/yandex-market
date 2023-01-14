package com.yandex.market.shopservice.dto.branch;

import com.yandex.market.shopservice.model.branch.PickupPointPartner;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponseDto {
    private Set<PickupPointPartner> pickupPointPartners;
    private Set<DeliveryZoneDto> deliveryZones;
    private Set<DeliveryIntervalDto> deliveryIntervals;
}