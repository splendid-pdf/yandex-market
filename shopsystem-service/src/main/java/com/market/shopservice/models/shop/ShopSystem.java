package com.market.shopservice.models.shop;

import com.market.shopservice.models.branch.Branch;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shop_systems")
@EqualsAndHashCode(of = {"externalId"})
public class ShopSystem {

    @Id
    @SequenceGenerator(name = "shop_seq", sequenceName = "shop_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shop_seq")
    private Long id;

    private UUID externalId;

    private String name;

    private String token;

    @Embedded
    private Support support;

    @Embedded
    private Location legalEntityAddress;

    @OneToMany(mappedBy = "shopSystem", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<SpecialOffer> specialOffers = new HashSet<>();


    @OneToMany(mappedBy = "shopSystem", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Branch> branches = new HashSet<>();

    private String logoUrl;

    private float rating;

    private boolean isDisabled = false;

    private boolean isTest = false;

    public void addSpecialOffer(SpecialOffer specialOffer) {
        specialOffer.setShopSystem(this);
        specialOffers.add(specialOffer);
    }

    public void removeSpecialOffer(SpecialOffer specialOffer) {
        specialOffers.remove(specialOffer);
    }

    public void addBranch(Branch branch) {
        branch.setShopSystem(this);
        branches.add(branch);
    }

    public void removeBranch(Branch branch) {
        branches.remove(branch);
    }

    public ShopSystem(UUID externalId, String name, String token, Support support, Location legalEntityAddress, Set<SpecialOffer> specialOffers, Set<Branch> branches, String logoUrl, float rating) {
        this.externalId = externalId;
        this.name = name;
        this.token = token;
        this.support = support;
        this.legalEntityAddress = legalEntityAddress;
        this.specialOffers = specialOffers;
        this.branches = branches;
        this.logoUrl = logoUrl;
        this.rating = rating;
    }
}
