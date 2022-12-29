package com.yandex.market.shopservice.dto.branch;

import com.yandex.market.shopservice.dto.LocationDto;
import com.yandex.market.shopservice.model.branch.Delivery;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Field \"Branch Name\" must not be empty")
    private String branchName;

    @NotBlank(message = "Field \"Company Name\" must not be empty")
    private String companyName;

    private String companyLogoUrl;

    private String token;

    @NotBlank(message = "Field \"OGRN\" must not be empty")
    private String ogrn;

    private LocationDto location;

    private ContactDto contact;

    private Delivery delivery;
}
