package com.yandex.market.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record TypeDto(
        @NotBlank
        String name,
        @NotNull
        Set<CharacteristicDto> characteristicDto,
        @NotNull
        Set<RoomDto> roomDto) {
}