package com.market.shopservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shop_systems")
public class ShopSystem {

    @Id
    @Column(name = "shop_systems_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "support_id")
    private Support support;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "legal_entity_address_id")
    private LegalEntityAddress legalEntityAddress;

    @OneToMany(mappedBy = "shopSystem", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "shopSystem", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<SpecialOffer> specialOffers = new HashSet<>();

    private String logoUrl;

    private Float rating;

    private boolean disabled;

    private boolean isTest;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopSystem that = (ShopSystem) o;
        return id.equals(that.id) && name.equals(that.name) && legalEntityAddress.equals(that.legalEntityAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, legalEntityAddress);
    }
}
