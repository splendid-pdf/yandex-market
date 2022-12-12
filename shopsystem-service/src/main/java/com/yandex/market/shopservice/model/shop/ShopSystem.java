package com.yandex.market.shopservice.model.shop;

import com.yandex.market.shopservice.model.branch.Branch;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ShopSystem {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shop_seq")
    @SequenceGenerator(name = "shop_seq", sequenceName = "shop_sequence", allocationSize = 1)
    private Long id;

    private UUID externalId;

    private String name;

    private String token;

    @Embedded
    private Support support;

    @Embedded
    private Location legalEntityAddress;

    @OneToMany(mappedBy = "shopSystem", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Branch> branches = new HashSet<>();

    @OneToMany(mappedBy = "shopSystem", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<SpecialOffer> specialOffers = new HashSet<>();

    private String logoUrl;

    private float rating;

    private boolean isDisabled;

    private boolean isTest;

    public void addBranch(Branch branch) {
        branch.setShopSystem(this);
        branches.add(branch);
    }

    public void removeBranch(Branch branch) {
        branches.remove(branch);
    }

    public void addSpecialOffer(SpecialOffer specialOffer) {
        specialOffer.setShopSystem(this);
        specialOffers.add(specialOffer);
    }

    public void removeSpecialOffer(SpecialOffer specialOffer) {
        specialOffers.remove(specialOffer);
    }
}
