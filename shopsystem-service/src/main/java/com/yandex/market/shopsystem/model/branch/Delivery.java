package com.yandex.market.shopsystem.model.branch;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Delivery {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_seq")
    @SequenceGenerator(name = "delivery_seq", sequenceName = "delivery_sequence", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Branch branch;

    private boolean hasDelivery;

    private boolean hasExpressDelivery;

    private boolean hasDeliveryToPickupPoint;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = PickupPointPartner.class, fetch = FetchType.LAZY)
    @JoinTable(name = "pickup_point_partners", joinColumns = @JoinColumn(name = "pickup_partner_id"))
    private Set<PickupPointPartner> pickupPointPartners = new HashSet<>();

    @OneToMany(mappedBy = "delivery", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DeliveryZone> deliveryZones = new HashSet<>();

    @OneToMany(mappedBy = "delivery", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DeliveryInterval> deliveryIntervals = new HashSet<>();

    public void addDeliveryZone(DeliveryZone deliveryZone) {
        deliveryZone.setDelivery(this);
        deliveryZones.add(deliveryZone);
    }

    public void removeDeliveryZone(DeliveryZone deliveryZone) {
        deliveryZones.remove(deliveryZone);
    }

    public void addDeliveryInterval(DeliveryInterval deliveryInterval) {
        deliveryInterval.setDelivery(this);
        deliveryIntervals.add(deliveryInterval);
    }

    public void removeDeliveryInterval(DeliveryInterval deliveryInterval) {
        deliveryIntervals.remove(deliveryInterval);
    }
}