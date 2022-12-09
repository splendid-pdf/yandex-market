package com.market.shopservice.models.branch;

import lombok.Getter;

@Getter
public enum PickupPointPartners {

    YANDEXMARKET("Яндекс-Маркет"),
    OZON("OZON"),
    WILDBERRIES("Вайлдбериз"),
    FIVEPOST("5POST"),
    SDEK("СДЭК"),
    POSTE("Почта");

    private final String title;

    PickupPointPartners(String title) {
        this.title = title;
    }
}
