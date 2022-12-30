package com.yandex.market.shopservice.dto.branch;

import com.yandex.market.shopservice.dto.LocationDto;
import com.yandex.market.shopservice.model.branch.Delivery;
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
    private String branchName;
    private String companyName;
    private String companyLogoUrl;
    private String token;
    private String ogrn;
    private LocationDto location;
    private ContactDto contact;
    private Delivery delivery;
}