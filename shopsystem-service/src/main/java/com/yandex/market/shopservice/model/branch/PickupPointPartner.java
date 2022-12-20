package com.yandex.market.shopservice.model.branch;

import lombok.Getter;

@Getter
public enum PickupPointPartner {
    YANDEX_MARKET("Яндекс-Маркет"),
    OZON("OZON"),
    WILDBERRIES("Вайлдбериз"),
    FIVE_POST("5POST"),
    CDEK("СДЭК"),
    POST_OFFICE("Почта");

    private final String title;

    PickupPointPartner(String title) {
        this.title = title;
    }
}
