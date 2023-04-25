package com.marketplace.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponseDto(
        UUID id,
        String firstName,
        String lastName,
        String phone,
        String email,
        String sex,
        String photoId,
        LocationDto location
) {
}