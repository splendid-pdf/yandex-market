package com.market.shopservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "shops")
public class ShopSystem {
    @Id
    @SequenceGenerator(name = "sequence_id_shop", sequenceName = "sequence_id_shop")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_id_shop")
    private Long id;
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID externalId;
    private String name;
    private String token;

    @Embedded
    private Support support;

    @Embedded
    private LegalEntityAddress address;

    // TODO: переделать
    @OneToMany(mappedBy = "shopSystem",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<SpecialOffer> offers;

    private String logoUrl;
    private Double rating;

    private Boolean disabled = false;
    private Boolean isTest = false;

    public ShopSystem(String name, String token, Support support, LegalEntityAddress address, Set<SpecialOffer> offers, String logoUrl, Double rating) {
        this.name = name;
        this.token = token;
        this.support = support;
        this.address = address;
        this.offers = offers;
        this.logoUrl = logoUrl;
        this.rating = rating;
    }
}
