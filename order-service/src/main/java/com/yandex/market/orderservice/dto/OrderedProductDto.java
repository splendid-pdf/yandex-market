package com.yandex.market.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record OrderedProductDto(
        @NotNull(message = "ProductId field must not be empty")
        UUID productId,
        @Positive(message = "Amount must be grater 0")
        int amount,
        @NotBlank(message = "Country field must not be empty")
        String name,
        @Positive(message = "Price must be greater than 0")
        double price,
        @NotBlank(message = "Country field must not be empty")
        String description,
        @NotBlank(message = "Photo url field must not be empty")
        String photoUrl,
        UUID branchId,
        UUID shopSystemId) {

}