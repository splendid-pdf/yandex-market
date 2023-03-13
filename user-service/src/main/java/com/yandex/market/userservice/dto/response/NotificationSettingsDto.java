package com.yandex.market.userservice.dto.response;

public record NotificationSettingsDto(
        Boolean isAllowedToReceiveOnAddress,
        Boolean isAllowedToSendPromotionsAndMailingLists,
        Boolean isAllowedToSendPopularArticles
) {
}