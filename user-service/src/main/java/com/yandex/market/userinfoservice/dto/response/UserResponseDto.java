package com.yandex.market.userinfoservice.dto.response;

import java.time.LocalDate;
import java.util.List;
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
        List<ContactDto> contacts,
        NotificationSettingsDto notificationSettings,
        String photoId
) {

}

