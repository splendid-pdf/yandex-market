package com.yandex.market.userinfoservice.mapper;

import com.yandex.market.userinfoservice.model.NotificationSettings;
import org.openapitools.api.model.NotificationSettingsDto;
import org.springframework.stereotype.Component;

@Component
public class NotificationSettingsMapper {

    public NotificationSettingsDto notificationSettingsToDto(NotificationSettings notificationSettings) {
        NotificationSettingsDto notificationSettingsDto = new NotificationSettingsDto();
        notificationSettingsDto.setIsAllowedToReceiveOnAddress(notificationSettings.isAllowedToReceiveOnAddress());
        notificationSettingsDto.setIsAllowedToSendPopularArticles(notificationSettings.isAllowedToSendPopularArticles());
        notificationSettingsDto
                .setIsAllowedToSendPromotionsAndMailingLists(notificationSettings.isAllowedToSendDiscountsAndPromotionsMailingLists());
        return notificationSettingsDto;
    }
    public NotificationSettings mapNotificationSettings(NotificationSettingsDto notificationSettingsDto) {
        return NotificationSettings.builder()
                .isAllowedToSendDiscountsAndPromotionsMailingLists(notificationSettingsDto.getIsAllowedToSendPromotionsAndMailingLists())
                .isAllowedToSendPopularArticles(notificationSettingsDto.getIsAllowedToSendPopularArticles())
                .isAllowedToReceiveOnAddress(notificationSettingsDto.getIsAllowedToReceiveOnAddress())
                .build();
    }

}
