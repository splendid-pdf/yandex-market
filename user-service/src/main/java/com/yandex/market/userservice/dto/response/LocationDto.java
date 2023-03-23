package com.yandex.market.userservice.dto.response;

public record LocationDto(
        String country,
        String city,
        String region,
        String street,
        String postcode,
        String houseNumber,
        String apartNumber
) {
}