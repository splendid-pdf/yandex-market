package com.yandex.market.userservice.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

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
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        LocalDate birthday,
        String sex,
        LocationDto location,
        List<ContactDto> contacts,
        NotificationSettingsDto notificationSettings,
        String photoId
) {

}

