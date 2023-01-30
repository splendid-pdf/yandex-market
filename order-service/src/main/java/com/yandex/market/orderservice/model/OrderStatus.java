package com.yandex.market.orderservice.model;

public enum OrderStatus {

    CREATED,
    IN_PROCESSING,
    TRANSFERRED_TO_DELIVERY,
    ON_THE_WAY,
    DELIVERED_TO_PICKUP_POINT,
    COMPLETED,
    CANCELED
}