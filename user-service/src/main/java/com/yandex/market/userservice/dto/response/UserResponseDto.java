package com.yandex.market.userservice.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponseDto(
        UUID externalId,
        String firstName,
        String middleName,
        String lastName,
        String phone,
        String email,
        LocalDate birthday,
        String sex,
        LocationDto location,
        NotificationSettingsDto notificationSettings,
        String photoUrl
) {
}