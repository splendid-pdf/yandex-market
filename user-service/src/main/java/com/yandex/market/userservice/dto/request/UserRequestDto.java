package com.yandex.market.userservice.dto.request;

import com.yandex.market.userservice.dto.response.LocationDto;
import com.yandex.market.userservice.dto.response.NotificationSettingsDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record UserRequestDto(
        String firstName,
        String middleName,
        String lastName,
        String phone,
        String email,
        String password,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate birthday,
        String sex,
        LocationDto location,
        NotificationSettingsDto notificationSettingsDto,
        String photoUrl
) {
}