package com.yandex.market.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

@Schema
public record ProductImageDto(

        @NotNull(message = "The flag whether the photo is the main photo of the product must be specified")
        boolean isMain,

        @URL(message = "The URL must be valid")
        @NotBlank(message = "The link to the image must be specified")
        String url
) {
}
