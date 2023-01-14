package com.yandex.market.shopservice.dto.branch;

import com.yandex.market.shopservice.model.branch.PickupPointPartner;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponseDto {
    private Set<PickupPointPartner> pickupPointPartners = new HashSet<>();
    private Set<DeliveryZoneDto> deliveryZones = new HashSet<>();
    private Set<DeliveryIntervalDto> deliveryIntervals = new HashSet<>();
}