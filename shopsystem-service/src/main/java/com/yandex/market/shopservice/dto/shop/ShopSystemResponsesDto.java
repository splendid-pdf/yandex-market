package com.yandex.market.shopservice.dto.shop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yandex.market.shopservice.dto.LocationDto;
import com.yandex.market.shopservice.model.branch.Branch;
import com.yandex.market.shopservice.model.shop.SpecialOffer;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopSystemResponsesDto {
    @NotBlank(message = "Field \"Name\" must not be empty")
    private String name;
    private String token;
    private SupportDto support;

    private LocationDto legalEntityAddress;

    @JsonIgnoreProperties(value = {"shopSystem"})
    private Set<SpecialOffer> specialOffers = new HashSet<>();

    @JsonIgnoreProperties(value = {"shopSystem"})
    private Set<Branch> branches = new HashSet<>();

    private String logoUrl;
    private float rating;
}
