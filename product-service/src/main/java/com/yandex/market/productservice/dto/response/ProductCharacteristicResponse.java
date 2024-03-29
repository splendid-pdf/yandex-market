package com.yandex.market.productservice.dto.response;

import java.util.UUID;

public record ProductCharacteristicResponse(
        UUID id,
        String name,
        Object value,
        String groupCharacteristic
) {
}
