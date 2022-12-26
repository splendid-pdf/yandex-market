package com.yandex.market.shopservice.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record LocationDto(
        @NotBlank(message = "\"Country\" field must not be empty")
        @Size(max = 128, message = "\"Country\" field can not be more than 128 characters")
        String country,

        @NotBlank(message = "\"Region\" field must not be empty")
        @Size(max = 128, message = "\"Region\" field can not be more than 128 characters")
        String region,

        @NotBlank(message = "\"City\" field must not be empty")
        @Size(max = 128, message = "\"City\" field can not be more than 128 characters")
        String city,

        @NotBlank(message = "\"Street\" field must not be empty")
        @Size(max = 128, message = "\"Street\" field can not be more than 128 characters")
        String street,

        @NotBlank(message = "\"House number\" field must not be empty")
        @Size(max = 5, message = "\"House number\" field can not be more than 5 characters")
        String houseNumber,

        @Size(max = 5, message = "\"Office number\" field can not be more than 5 characters")
        String officeNumber,

        @NotBlank(message = "\"Postcode\" field must not be empty")
        @Pattern(regexp = "^\\d{6}$", message = "Invalid \"Postcode\" entered")
        @Size(max = 6, min = 6, message = "\"Postcode\" field must be 6 characters long")
        String postcode) {
}

