package com.yandex.market.userservice.mapper;

import com.yandex.market.userservice.dto.response.NotificationSettingsDto;
import com.yandex.market.userservice.model.NotificationSettings;
import org.springframework.stereotype.Component;

@Component
public class NotificationSettingsMapper {

    public NotificationSettingsDto notificationSettingsToDto(NotificationSettings notificationSettings) {
        return new NotificationSettingsDto(
                notificationSettings.isAllowedToReceiveOnAddress(),
                notificationSettings.isAllowedToSendDiscountsAndPromotionsMailingLists(),
                notificationSettings.isAllowedToReceiveOnAddress());
    }

    public NotificationSettings mapNotificationSettings(NotificationSettingsDto notificationSettingsDto) {
        return NotificationSettings.builder()
                .isAllowedToSendDiscountsAndPromotionsMailingLists(notificationSettingsDto.isAllowedToSendPromotionsAndMailingLists())
                .isAllowedToSendPopularArticles(notificationSettingsDto.isAllowedToSendPopularArticles())
                .isAllowedToReceiveOnAddress(notificationSettingsDto.isAllowedToReceiveOnAddress())
                .build();
    }
}