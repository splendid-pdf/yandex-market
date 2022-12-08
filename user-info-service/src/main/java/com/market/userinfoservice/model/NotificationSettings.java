package com.market.userinfoservice.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class NotificationSettings {
    private String emailToSend;
    private boolean isAllowedToStoreResponseToMyReviews;
    private boolean isAllowedToSendDiscountsAndPromotionsMailingLists;
    private boolean isAllowedToSendPopularArticles;
}
