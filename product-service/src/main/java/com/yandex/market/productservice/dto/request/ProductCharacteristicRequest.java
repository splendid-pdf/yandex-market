package com.yandex.market.productservice.dto.request;

import com.yandex.market.productservice.model.ValueType;

public record ProductCharacteristicRequest(
        String value,
        String name,
        ValueType valueType
) {
}
