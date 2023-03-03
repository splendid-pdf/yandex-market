package com.yandex.market.userinfoservice.dto.response;

public record NotificationSettingsDto(
        Boolean isAllowedToReceiveOnAddress,
        Boolean isAllowedToSendPromotionsAndMailingLists,
        Boolean isAllowedToSendPopularArticles
) {
}