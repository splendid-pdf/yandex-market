package com.yandex.market.userinfoservice.dto.response;

public record LocationDto(
        String country,
        String city,
        String region,
        String street,
        String postcode,
        String houseNumber,
        String apartNumber,
        Double latitude,
        Double longitude
) {
}