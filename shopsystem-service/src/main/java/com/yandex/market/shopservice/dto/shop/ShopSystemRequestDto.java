package com.yandex.market.shopservice.dto.shop;

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
public class ShopSystemRequestDto {
    @NotBlank(message = "Field \"Name\" must not be empty")
    private String name;
    private String token;
    private SupportDto support;

    private LocationDto legalEntityAddress;

    private Set<SpecialOffer> specialOffers = new HashSet<>();
    private Set<Branch> branches = new HashSet<>();

    private String logoUrl;
}
