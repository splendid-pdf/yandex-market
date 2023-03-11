package com.yandex.market.userinfoservice.mapper;

import com.yandex.market.mapper.Mapper;
import com.yandex.market.userinfoservice.model.Sex;
import com.yandex.market.userinfoservice.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.api.model.UserRequestDto;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
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
                .firstName(userRequestDto.getFirstName())
                .middleName(userRequestDto.getMiddleName())
                .lastName(userRequestDto.getLastName())
                .phone(userRequestDto.getPhone())
                .email(userRequestDto.getEmail())
                .login(trimEmailForLogin(userRequestDto))
                .password(userRequestDto.getPassword())
                .birthday(userRequestDto.getBirthday())
                .sex(getSex(userRequestDto.getSex()))
                .location(locationMapper.map(userRequestDto.getLocation()))
                .notificationSettings(
                        notificationSettingsMapper.mapNotificationSettings(userRequestDto.getNotificationSettings()))
                .photoId(userRequestDto.getPhotoId())
                .build();

        Stream.ofNullable(userRequestDto.getContacts())
                .flatMap(Collection::stream)
                .filter(contact -> ObjectUtils.allNotNull(contact.getValue(), contact.getType()))
                .map(contactMapper::map)
                .forEach(user::addContact);
        return user;
    }

    private String trimEmailForLogin(UserRequestDto userRequestDto) {
        return userRequestDto.getEmail().replaceAll("@[a-zA-Z0-9.-]+$", "");
    }

    private Sex getSex(String sex) {
        return StringUtils.isNotEmpty(sex) ? Sex.valueOf(sex) : Sex.NONE;
    }
}