package com.yandex.market.basketservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Информация о добавленном в корзину товаре")
public interface ItemResponse {

    @Schema(description = "Идентификатор товара")
    UUID getProductId();

    @Schema(description = "Количество данного товара в корзине (шт.)", minimum = "1")
    Integer getTotalNumberItemsInBasket();
}
