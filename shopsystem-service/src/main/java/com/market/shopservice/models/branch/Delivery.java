package com.market.shopservice.models.branch;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "deliveries")
public class Delivery {

    @Id
    @SequenceGenerator(name = "delivery_seq", sequenceName = "delivery_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_seq")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Branch branch;

    private boolean hasDelivery;

    private boolean hasExpressDelivery;

    private boolean hasDeliveryToPickupPoint;

    @ElementCollection
    // TODO переделать;
    private Set<PickupPointPartners> pickupPointPartners = new HashSet<>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Delivery delivery = (Delivery) o;
        return id != null && Objects.equals(id, delivery.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
