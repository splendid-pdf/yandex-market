package com.yandex.market.userinfoservice.dto.request;

import com.yandex.market.userinfoservice.dto.response.ContactDto;
import com.yandex.market.userinfoservice.dto.response.LocationDto;
import com.yandex.market.userinfoservice.dto.response.NotificationSettingsDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

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
        List<ContactDto> contacts,
        NotificationSettingsDto notificationSettingsDto,
        String photoId
) {
}