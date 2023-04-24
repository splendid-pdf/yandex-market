package com.marketplace.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LocationDto(
        String city,
        String deliveryAddress
) {
}