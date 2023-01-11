package com.yandex.market.shopservice.dto.branch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yandex.market.shopservice.dto.LocationDto;
import com.yandex.market.shopservice.model.branch.SupportedPaymentMethods;
import com.yandex.market.shopservice.model.shop.SpecialOffer;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchResponseDto {
    private UUID shopSystemExternalId;
    private UUID branchExternalId;
    private String companyName;
    private String branchName;
    private String companyLogoUrl;
    private String ogrn;
    private LocationDto location;
    private ContactResponseDto contact;
    private SupportedPaymentMethods paymentMethods;
    private boolean hasDelivery;
    private boolean hasExpressDelivery;
    private boolean hasDeliveryToPickupPoint;

    private DeliveryResponseDto delivery;
    @JsonIgnoreProperties(value = {"branch"})
    private Set<SpecialOffer> discounts = new HashSet<>();
}