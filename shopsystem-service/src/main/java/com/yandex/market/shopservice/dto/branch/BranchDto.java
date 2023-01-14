package com.yandex.market.shopservice.dto.branch;

import com.yandex.market.shopservice.dto.LocationDto;
import com.yandex.market.shopservice.model.branch.SupportedPaymentMethods;
import jakarta.persistence.Embedded;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchDto {

    @NotNull(message = "\"UUID of Shop System\" field must not be empty")
    private UUID shopSystemExternalId;

    @NotBlank(message = "\"Name\" field must not be empty")
    @Size(max = 128, message = "\"Name\" field can not be more than 128 characters")
    private String name;

    @NotBlank(message = "\"Token\" field must not be empty")
    private String token;

    @NotBlank(message = "\"OGRN\" field must not be empty")
    @Pattern(regexp = "^\\d{13}$", message = "Invalid \"OGRN\" entered")
    @Size(max = 13, min = 13, message = "\"OGRN\" field must be 13 characters long")
    private String ogrn;

    @Valid
    @NotNull(message = "\"Location of Branch\" field must not be empty")
    private LocationDto location;

    @Valid
    @NotNull(message = "\"Contact of Branch\" field must not be empty")
    private ContactDto contact;

    @NotNull(message = "\"Delivery of Branch\" field must not be empty")
    private DeliveryDto delivery;


    @Embedded
    private SupportedPaymentMethods paymentMethods;
}
