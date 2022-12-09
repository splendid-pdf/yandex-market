package com.market.shopservice.models.department;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "deliveries")
public class Delivery {

    @Id
    @SequenceGenerator(name = "delivery_seq", sequenceName = "delivery_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_seq")
    private Long id;

    @OneToOne
    private Branch branch;

    private boolean hasDelivery;

    private boolean hasExpressDelivery;

    @Enumerated(value = EnumType.STRING)
    // TODO переделать;
    private Set<PickupPointPartners> pickupPointPartners = new HashSet<>();

    // TODO deliveryZones;

    // TODO deliveryTimeIntervals;
}
