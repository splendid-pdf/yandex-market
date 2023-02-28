package com.yandex.market.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Schema
public record TypeDto(
        @NotBlank
        String name,
        @NotNull
        Set<TypeCharacteristicDto> typeCharacteristicDto,
        @NotNull
        Set<RoomDto> roomDto) {
}