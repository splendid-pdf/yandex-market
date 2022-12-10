package com.market.shopservice.dto;

import com.market.shopservice.models.branch.Branch;
import com.market.shopservice.models.shop.Location;
import com.market.shopservice.models.shop.SpecialOffer;
import com.market.shopservice.models.shop.Support;
import jakarta.persistence.Embedded;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopSystemDto {
    private UUID externalId;
    private String name;
    private String token;

    @Embedded
    private Support support;

    @Embedded
    private Location legalEntityAddress;

    private Set<SpecialOffer> specialOffers = new HashSet<>();

    private Set<Branch> branches = new HashSet<>();

    private String logoUrl;
    private float rating;
}
