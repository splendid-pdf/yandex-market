package com.marketplace.aggregatorservice.dto;

public record AddressRequestDto(
        String country,
        String region,
        String city,
        String postCode,
        String street,
        String houseNumber,
        String officeNumber) {
}