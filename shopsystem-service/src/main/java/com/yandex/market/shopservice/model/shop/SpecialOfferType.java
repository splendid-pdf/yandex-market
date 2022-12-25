package com.yandex.market.shopservice.model.shop;

import lombok.Getter;

@Getter
public enum SpecialOfferType {
    SALE("Распродажа"),
    PROFIT("Выгодные комплекты"),
    DISCOUNT("Скидки и предложения"),
    INSTALLMENT("Рассрочка или выгода"),
    PERSONAL_OFFER("Персональное предложение");

    private final String title;

    SpecialOfferType(String title) {
        this.title = title;
    }
}