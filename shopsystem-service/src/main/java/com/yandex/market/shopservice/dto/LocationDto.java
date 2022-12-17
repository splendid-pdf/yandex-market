package com.yandex.market.shopservice.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record LocationDto(
        @Size(max = 128, message = "Country name can not be more than 128 characters")
        String country,
        @Size(max = 128, message = "Region name can not be more than 128 characters")
        String region,
        @Size(max = 128, message = "City name can not be more than 128 characters")
        String city,
        @Size(max = 255, message = "Street name can not be more than 255 characters")
        String street,
        @Positive(message = "House number can not be negative")
        int houseNumber,
        @Positive(message = "Office number can not be negative")
        int officeNumber,
        @Size(max = 10, message = "Postcode can not be more than 10 characters")
        String postcode) {
}

