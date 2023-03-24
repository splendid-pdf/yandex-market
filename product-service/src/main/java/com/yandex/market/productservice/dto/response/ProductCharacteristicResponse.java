package com.yandex.market.productservice.dto.response;

import java.util.UUID;

public record ProductCharacteristicResponse(
        UUID id,
        Object value
) {
}
