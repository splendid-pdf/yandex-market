package com.yandex.market.userservice.dto.response;

import java.util.UUID;

public record UserResponseDto(
        UUID externalId,
        String firstName,
        String lastName,
        String phone,
        String email,
        String sex,
        String photoUrl,
        LocationDto location
) {
}