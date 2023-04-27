package com.yandex.market.basketservice.dto;

import java.util.UUID;

public interface ItemResponse {
    UUID getProductId();
    Integer getTotalNumberItemsInBasket();
}
