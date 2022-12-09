package com.market.shopservice.models.shop;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shop_systems")
public class ShopSystem {

    @Id
    @Column(nullable = false)
    @SequenceGenerator(name = "shop_seq", sequenceName = "shop_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shop_seq")
    private Long id;

    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID externalId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String token;

    @Embedded
    private Support support;

    @Embedded
    private LegalEntityAddress legalEntityAddress;

    @OneToMany(mappedBy = "shopSystem", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<SpecialOffer> specialOffers = new HashSet<>();

    private String logoUrl;

    private float rating;

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
