package com.yandex.market.productservice.dto.request;

import com.yandex.market.productservice.dto.ProductImageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.UUID;

@Schema
public record CreateProductRequest(
        @NotBlank(message = "The name of the product must be indicated")
        @Size(min = 3, max = 128, message = "The product name must be in the range from 3 to 128 characters")
        String name,

        @Size(max = 200, message = "The product description must be in the range from 0 to 200 characters")
        String description,

        @Size(max = 200, message = "The article of the product must be in the range from 0 to 200 characters")
        String articleFromSeller,

        @NotBlank(message = "The brand name must be specified")
        @Size(max = 50, message = "The brand name must be in the range of 50 characters")
        String brand,

        @NotNull(message = "The price of the product must be specified")
        @Positive(message = "The price of the product must be above zero")
        Long price,

        @NotNull(message = "The quantity of the product must be specified")
        @PositiveOrZero(message = "The quantity of the product should not be negative")
        Long count,

        @NotNull(message = "The product type identifier must be specified")
        UUID typeId,

        @NotEmpty(message = "The product must have characteristics")
        List<@Valid ProductCharacteristicRequest> characteristics,

        @NotEmpty(message = "The product must have at least one image")
        List<@Valid ProductImageDto> images
) {
}