package com.yandex.market.shopservice.dto.shop;

import com.yandex.market.shopservice.dto.LocationDto;
import com.yandex.market.shopservice.dto.branch.BranchDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class ShopSystemResponsesDto {
    @NotBlank(message = "\"Name\" field must not be empty")
    private String name;

    @NotBlank(message = "\"Token\" field must not be empty")
    private String token;

    @Valid
    @NotNull(message = "\"Support contact of Shop\" field must not be empty")
    private SupportDto support;

    @Valid
    @NotNull(message = "\"Location of Shop\" field must not be empty")
    private LocationDto legalEntityAddress;

    private Set<@Valid SpecialOfferDto> specialOffers;

    private Set<@Valid BranchDto> branches;

    @Pattern(regexp = "^(https?:\\/\\/)?([\\w-]{1,32}\\.[\\w-]{1,32})[^\\s@]*$",
            message = "Invalid \"Logo URL\" entered")
    private String logoUrl;

    @Positive(message = "\"Rating of Shop\" field must not be negative")
    private float rating;
}
