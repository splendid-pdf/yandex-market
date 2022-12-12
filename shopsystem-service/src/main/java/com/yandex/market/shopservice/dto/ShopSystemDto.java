package com.yandex.market.shopservice.dto;

import com.yandex.market.shopservice.model.branch.Branch;
import com.yandex.market.shopservice.model.shop.SpecialOffer;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopSystemDto {
    @NotBlank(message = "Field \"Name\" must not be empty")
    private String name;
    private String token;

    @Embedded
    private SupportDto support;

    @Embedded
    private LocationDto legalEntityAddress;

    private Set<SpecialOffer> specialOffers = new HashSet<>();
    private Set<Branch> branches = new HashSet<>();

    private String logoUrl;
    private float rating;
}
