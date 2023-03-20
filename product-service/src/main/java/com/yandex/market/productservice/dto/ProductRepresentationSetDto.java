package com.yandex.market.productservice.dto;

import java.util.Set;
import java.util.UUID;

public record ProductRepresentationSetDto(
        Set<UUID> productIdentifiers) {
}