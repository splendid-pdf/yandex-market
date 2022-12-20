package com.yandex.market.userinfoservice.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Setter
@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettings {
    private String emailToSend;
    private Boolean isAllowedToStoreResponseToMyReviews;
    private Boolean isAllowedToSendDiscountsAndPromotionsMailingLists;
    private Boolean isAllowedToSendPopularArticles;
}

