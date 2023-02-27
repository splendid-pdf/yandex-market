package com.yandex.market.userinfoservice.mapper;

import com.yandex.market.mapper.Mapper;
import com.yandex.market.userinfoservice.model.Sex;
import com.yandex.market.userinfoservice.model.User;
import io.micrometer.common.util.StringUtils;
import io.netty.util.internal.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.model.UserResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

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
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setExternalId(user.getExternalId());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setMiddleName(user.getMiddleName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setPhone(user.getPhone());
        userResponseDto.setPhotoId(user.getPhotoId());

        if (user.getSex() == null) {
            userResponseDto.setSex(Sex.NONE.name());
        } else {
            userResponseDto.setSex(user.getSex().name());
        }

        userResponseDto.setBirthday(user.getBirthday());
        userResponseDto.setContacts(Stream.ofNullable(user.getContacts())
                .flatMap(Collection::stream)
                .map(contactMapper::mapToDto)
                .toList());

        if (user.getLocation() != null) {
            userResponseDto.setLocation(locationMapper.mapToDto(user.getLocation()));
        }

        if (user.getNotificationSettings() != null) {
            userResponseDto.setNotificationSettings(notificationSettingsMapper
                    .notificationSettingsToDto(user.getNotificationSettings()));
        }

        return userResponseDto;
    }
}