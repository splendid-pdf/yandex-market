package com.yandex.market.shopservice.dto.branch;

import com.yandex.market.shopservice.model.branch.PickupPointPartner;
import jakarta.validation.Valid;
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
    private Set<@Valid DeliveryZoneDto> deliveryZones = new HashSet<>();
    private Set<@Valid DeliveryIntervalDto> deliveryIntervals = new HashSet<>();
}