package com.yandex.market.shopsystem.model.branch;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery_zones")
public class DeliveryZone {

    @Id
    @EqualsAndHashCode.Include
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
