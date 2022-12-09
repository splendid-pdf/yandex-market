package com.market.shopservice.models.branch;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery_zones")
public class DeliveryZone {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_zone_seq")
    @SequenceGenerator(name = "delivery_zone_seq", sequenceName = "delivery_zone_sequence", allocationSize = 1)
    private Long id;

    private String zoneId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Delivery delivery;

    private int radiusInMeters;

    private double standardDeliveryPrice;

    private double expressDeliveryPrice;
}
