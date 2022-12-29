package com.yandex.market.shopservice.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopSystemBranchInfoDto {
    private UUID shopSystemExternalId;
    private String companyName;
    private String companyLogoUrl;
}