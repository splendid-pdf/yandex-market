package com.yandex.market.userservice.mapper;

import com.yandex.market.mapper.Mapper;
import com.yandex.market.userservice.dto.request.UserRequestDto;
import com.yandex.market.userservice.model.Sex;
import com.yandex.market.userservice.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRequestMapper implements Mapper<UserRequestDto, User> {
    private final LocationMapper locationMapper;
    private final NotificationSettingsMapper notificationSettingsMapper;

    @Override
    public User map(UserRequestDto userRequestDto) {
        return User.builder()
                .externalId(UUID.randomUUID())
                .firstName(userRequestDto.firstName())
                .middleName(userRequestDto.middleName())
                .lastName(userRequestDto.lastName())
                .phone(userRequestDto.phone())
                .email(userRequestDto.email())
                .login(trimEmailForLogin(userRequestDto))
                .password(userRequestDto.password())
                .birthday(userRequestDto.birthday())
                .sex(getSex(userRequestDto.sex()))
                .location(locationMapper.map(userRequestDto.location()))
                .notificationSettings(
                        notificationSettingsMapper.mapNotificationSettings(userRequestDto.notificationSettingsDto()))
                .photoUrl(userRequestDto.photoUrl())
                .build();
    }

    private String trimEmailForLogin(UserRequestDto userRequestDto) {
        return userRequestDto.email().replaceAll("@[a-zA-Z0-9.-]+$", "");
    }

    private Sex getSex(String sex) {
        return StringUtils.isNotEmpty(sex) ? Sex.valueOf(sex) : Sex.NONE;
    }
}