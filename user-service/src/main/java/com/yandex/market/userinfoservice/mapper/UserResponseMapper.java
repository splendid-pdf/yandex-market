package com.yandex.market.userinfoservice.mapper;

import com.yandex.market.mapper.Mapper;
import com.yandex.market.userinfoservice.dto.response.UserResponseDto;
import com.yandex.market.userinfoservice.model.User;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class UserResponseMapper implements Mapper<User, UserResponseDto> {

    private final ContactMapper contactMapper;
    private final LocationMapper locationMapper;

    private final NotificationSettingsMapper notificationSettingsMapper;

    @Override
    public UserResponseDto map(User user) {
        val sex = user.getSex() != null ? user.getSex().name() : null;

        val contactDtoList = Stream.ofNullable(user.getContacts())
                .flatMap(Collection::stream)
                .map(contactMapper::mapToDto)
                .toList();

        val locationDto = user.getLocation() != null ? locationMapper.mapToDto(user.getLocation()) : null;

        val notificationSettingsDto = user.getNotificationSettings() != null
                ? notificationSettingsMapper.notificationSettingsToDto(user.getNotificationSettings())
                : null;

        return new UserResponseDto(
                user.getExternalId(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getPhone(),
                user.getEmail(),
                user.getBirthday(),
                sex,
                locationDto,
                contactDtoList,
                notificationSettingsDto,
                user.getPhotoId()
        );
    }
}