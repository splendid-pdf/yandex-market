package com.marketplace.userservice.dto.request;

import com.marketplace.userservice.dto.response.LocationDto;

public record UserRequestDto(
        String firstName,
        String lastName,
        String phone,
        String email,
        String sex,
        String photoId,
        LocationDto location
) {
}