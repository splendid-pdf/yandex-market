package com.yandex.market.userservice.dto.request;

import com.yandex.market.userservice.dto.response.LocationDto;

public record UserRequestDto(
        String firstName,
        String lastName,
        String phone,
        String email,
        String sex,
        String photoUrl,
        LocationDto location
) {
}