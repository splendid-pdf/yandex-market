package com.yandex.market.shopservice.dto;

import jakarta.validation.constraints.Size;

public record LocationDto(
        @Size(max = 128, message = "A country of suspiciously long length")
        String country,
        @Size(max = 128, message = "A region of suspiciously long length")
        String region,
        @Size(max = 128, message = "A city of suspiciously long length")
        String city,
        @Size(max = 255, message = "A street of suspiciously long length")
        String street,
        String houseNumber,
        int officeNumber,
        @Size(max = 10, message = "A postcode of suspiciously long length")
        String postcode) {
}

