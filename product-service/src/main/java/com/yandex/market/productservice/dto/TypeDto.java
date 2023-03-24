package com.yandex.market.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

@Schema
public record TypeDto(
        @NotBlank
        UUID id,
        String name
) {
}