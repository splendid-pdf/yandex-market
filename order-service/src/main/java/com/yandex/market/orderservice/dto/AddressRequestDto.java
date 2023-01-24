package com.yandex.market.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequestDto(
        @NotBlank(message = "Country field must not be empty")
        String country,
        @NotBlank(message = "Region field must not be empty")
        String region,
        @NotBlank(message = "City field must not be empty")
        String city,
        @NotBlank(message = "Postcode field must not be empty")
        @Size(max = 6, min = 6, message = "Address: Postcode field must be 6 characters long")
        String postCode,
        @NotBlank(message = "Street field must not be empty")
        String street,
        @NotBlank(message = "House number field must not be empty")
        String houseNumber,
        @NotBlank(message = "Office number field must not be empty")
        String officeNumber) {
}