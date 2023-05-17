package com.yandex.market.basketservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Информация о количестве добавленных товаров в корзину")
public record CountItemsResponse(
        @Schema(name = "itemsCount", description = "Общее количество товаров в корзине (шт.)")
        Integer itemsCount
) {
}
