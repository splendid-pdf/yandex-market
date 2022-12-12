package com.yandex.market.shopservice.dto;

import com.yandex.market.shopservice.model.branch.Branch;
import com.yandex.market.shopservice.model.shop.Location;
import com.yandex.market.shopservice.model.shop.SpecialOffer;
import com.yandex.market.shopservice.model.shop.Support;
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
