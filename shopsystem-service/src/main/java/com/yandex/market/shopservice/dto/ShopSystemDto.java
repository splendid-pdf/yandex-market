package com.yandex.market.shopservice.dto;

import com.yandex.market.shopservice.model.branch.Branch;
import com.yandex.market.shopservice.model.shop.SpecialOffer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopSystemDto {
    @NotBlank(message = "Field \"Name\" must not be empty")
    private String name;
    private String token;
    @Valid
    private SupportDto support;
    @Valid
    private LocationDto legalEntityAddress;

    private Set<SpecialOffer> specialOffers = new HashSet<>();
    private Set<Branch> branches = new HashSet<>();

    private String logoUrl;
    @Positive(message = "Rating can not be negative")
    private float rating;

}
