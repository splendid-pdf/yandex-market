package com.market.shopservice.models.department;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    private boolean hasDelivery;

    private boolean hasExpressDelivery;

    private Set<PickupPointPartners> pickupPointPartners;

    //TODO deliveryZones;

    //TODO deliveryTimeIntervals;
}
