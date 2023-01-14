package com.yandex.market.shopservice.dto.shop;

import com.yandex.market.shopservice.dto.LocationDto;
import com.yandex.market.shopservice.dto.branch.BranchDto;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class ShopSystemResponsesDto {
    private String name;

    private String token;

    private SupportDto support;

    private LocationDto legalEntityAddress;

    private Set<SpecialOfferDto> specialOffers;

    private Set<BranchDto> branches;

    private String logoUrl;

    private float rating;
}
