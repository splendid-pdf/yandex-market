package com.yandex.market.productservice.dto;

import jakarta.validation.constraints.NotBlank;

public record RoomDto(
        @NotBlank
        String name) {
}