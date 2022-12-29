package com.yandex.market.userinfoservice.mapper;

import com.yandex.market.userinfoservice.model.NotificationSettings;
import com.yandex.market.userinfoservice.model.Sex;
import com.yandex.market.userinfoservice.model.User;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.model.NotificationSettingsDto;
import org.openapitools.api.model.UserRequestDto;
import org.openapitools.api.model.UserResponseDto;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class UserMapper implements BiMapper<UserResponseDto, UserRequestDto, User> {

    private final ContactMapper contactMapper;
    private final LocationMapper locationMapper;

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
                .sex(Sex.valueOf(userRequestDto.getSex().name()))
                .location(locationMapper.map(userRequestDto.getLocation()))
                .notificationSettings(mapNotificationSettings(userRequestDto.getNotificationSettings()))
                .photoId(userRequestDto.getPhotoId())
                .build();

        Stream.ofNullable(userRequestDto.getContacts())
                .flatMap(Collection::stream)
                .map(contactMapper::map)
                .forEach(user::addContact);
        return user;
    }

    @Override
    public UserResponseDto mapToResponseDto(User user) {
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
        userResponseDto.setNotificationSettings(notificationSettingsToDto(user.getNotificationSettings()));
        return userResponseDto;
    }

    private NotificationSettingsDto notificationSettingsToDto(NotificationSettings notificationSettings) {
        NotificationSettingsDto notificationSettingsDto = new NotificationSettingsDto();
        notificationSettingsDto.setIsAllowedToReceiveOnAddress(notificationSettings.isAllowedToReceiveOnAddress());
        notificationSettingsDto.setIsAllowedToSendPopularArticles(notificationSettings.isAllowedToSendPopularArticles());
        notificationSettingsDto
                .setIsAllowedToSendPromotionsAndMailingLists(notificationSettings.isAllowedToSendDiscountsAndPromotionsMailingLists());
        return notificationSettingsDto;
    }

    private NotificationSettings mapNotificationSettings(NotificationSettingsDto notificationSettingsDto) {
        return NotificationSettings.builder()
                .isAllowedToSendDiscountsAndPromotionsMailingLists(notificationSettingsDto.getIsAllowedToSendPromotionsAndMailingLists())
                .isAllowedToSendPopularArticles(notificationSettingsDto.getIsAllowedToSendPopularArticles())
                .isAllowedToReceiveOnAddress(notificationSettingsDto.getIsAllowedToReceiveOnAddress())
                .build();
    }

    private String trimEmailForLogin(UserRequestDto userRequestDto) {
        return userRequestDto.getEmail().replaceAll("@[a-zA-Z0-9.-]+$", "");
    }
}
