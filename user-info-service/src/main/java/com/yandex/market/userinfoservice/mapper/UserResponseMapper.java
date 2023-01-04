package com.yandex.market.userinfoservice.mapper;

import com.yandex.market.mapper.Mapper;
import com.yandex.market.userinfoservice.model.User;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.model.UserResponseDto;
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
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setExternalId(user.getExternalId());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setMiddleName(user.getMiddleName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setPhone(user.getPhone());
        userResponseDto.setPhotoId(user.getPhotoId());
        userResponseDto.setSex(UserResponseDto.SexEnum.valueOf(user.getSex().name()));
        userResponseDto.setBirthday(user.getBirthday());
        userResponseDto.setContacts(Stream.ofNullable(user.getContacts())
                .flatMap(Collection::stream)
                .map(contactMapper::mapToDto)
                .toList());
        userResponseDto.setLocation(locationMapper.mapToDto(user.getLocation()));
        userResponseDto.setNotificationSettings(notificationSettingsMapper
                .notificationSettingsToDto(user.getNotificationSettings()));
        return userResponseDto;
    }
}
