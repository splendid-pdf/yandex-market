package com.yandex.market.productservice.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ProductCharacteristicUpdateDto (
        @NotBlank
        String value
){
}
