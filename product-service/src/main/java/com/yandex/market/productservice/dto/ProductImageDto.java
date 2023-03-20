package com.yandex.market.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema
public record ProductImageDto(

        boolean isMain,
        @NotBlank
        String url) {
}