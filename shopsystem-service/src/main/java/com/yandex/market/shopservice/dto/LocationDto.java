package com.yandex.market.shopservice.dto;

import jakarta.validation.constraints.Size;

public record LocationDto(
        @Size(max = 128)
        String country,
        @Size(max = 128)
        String region,
        @Size(max = 128)
        String city,
        @Size(max = 255)
        String street,
        String houseNumber,
        int officeNumber,
        @Size(max = 10)
        String postcode) {
}

