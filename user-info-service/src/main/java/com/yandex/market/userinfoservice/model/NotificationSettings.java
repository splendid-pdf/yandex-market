package com.yandex.market.userinfoservice.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettings {
    private String emailToSend;
    private boolean isAllowedToStoreResponseToMyReviews;
    private boolean isAllowedToSendDiscountsAndPromotionsMailingLists;
    private boolean isAllowedToSendPopularArticles;
}

