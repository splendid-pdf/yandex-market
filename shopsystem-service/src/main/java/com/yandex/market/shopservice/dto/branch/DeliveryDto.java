package com.yandex.market.shopservice.dto.branch;

import com.yandex.market.shopservice.model.branch.Branch;
import com.yandex.market.shopservice.model.branch.DeliveryInterval;
import com.yandex.market.shopservice.model.branch.DeliveryZone;
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
public class DeliveryDto {

    @Valid
    private Branch branch;

    private boolean hasDelivery;

    private boolean hasExpressDelivery;

    private boolean hasDeliveryToPickupPoint;

    @Valid
    // @NotNull
    private Set<PickupPointPartner> pickupPointPartners = new HashSet<>();

    @Valid
    // @NotNull
    private Set<DeliveryZone> deliveryZones = new HashSet<>();

    @Valid
    // @NotNull
    private Set<DeliveryInterval> deliveryIntervals = new HashSet<>();
}
