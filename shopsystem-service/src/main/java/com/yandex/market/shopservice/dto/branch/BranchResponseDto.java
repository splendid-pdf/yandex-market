package com.yandex.market.shopservice.dto.branch;

import com.yandex.market.shopservice.dto.LocationDto;
import com.yandex.market.shopservice.model.branch.SupportedPaymentMethods;
import lombok.*;

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

    //    private Set<SpecialOffer> discounts = new HashSet<>();
}