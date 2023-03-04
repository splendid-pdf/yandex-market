package com.yandex.market.userinfoservice.mapper;

import com.yandex.market.mapper.Mapper;
import com.yandex.market.userinfoservice.dto.request.UserRequestDto;
import com.yandex.market.userinfoservice.model.Sex;
import com.yandex.market.userinfoservice.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class UserRequestMapper implements Mapper<UserRequestDto, User> {

    private final ContactMapper contactMapper;
    private final LocationMapper locationMapper;
    private final NotificationSettingsMapper notificationSettingsMapper;

    @Override
    public User map(UserRequestDto userRequestDto) {
        User user = User.builder()
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
                .photoId(userRequestDto.photoId())
                .build();

        Stream.ofNullable(userRequestDto.contacts())
                .flatMap(Collection::stream)
                .filter(contact -> ObjectUtils.allNotNull(contact.value(), contact.type()))
                .map(contactMapper::map)
                .forEach(user::addContact);
        return user;
    }

    private String trimEmailForLogin(UserRequestDto userRequestDto) {
        return userRequestDto.email().replaceAll("@[a-zA-Z0-9.-]+$", "");
    }

    private Sex getSex(String sex) {
        return StringUtils.isNotEmpty(sex) ? Sex.valueOf(sex) : Sex.NONE;
    }
}