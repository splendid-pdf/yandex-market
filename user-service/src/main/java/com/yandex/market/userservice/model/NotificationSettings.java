package com.yandex.market.userservice.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Setter
@Getter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettings {
    private boolean isAllowedToReceiveOnAddress;
    private boolean isAllowedToSendDiscountsAndPromotionsMailingLists;
    private boolean isAllowedToSendPopularArticles;
}