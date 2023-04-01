package com.yandex.market.productservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema
public record ProductUpdateRequest(

        @Size(min = 3, max = 30, message = "The product name must be in the range from 3 to 30 characters")
        String name,

        @Size(max = 200, message = "The product description must be in the range from 0 to 200 characters")
        String description,

        @Size(max = 200, message = "The article of the product must be in the range from 0 to 200 characters")
        String articleFromSeller,

        @Size(max = 50, message = "The brand name must be in the range of 50 characters")
        String brand,

        @Positive(message = "The price of the product must be above zero")
        Long price,

        @PositiveOrZero(message = "The quantity of the product should not be negative")
        Long count,

        boolean isArchived,

        boolean isVisible

) {
}
