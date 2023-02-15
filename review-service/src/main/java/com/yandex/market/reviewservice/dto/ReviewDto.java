package com.yandex.market.reviewservice.dto;

import com.yandex.market.reviewservice.model.ReviewType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ReviewDto(
        UUID userId,
        UUID productId,
        ReviewType reviewType,
        String advantages,
        String disadvantages,
        String commentary,
        @Min(value = 1, message = "Оценка товара должна быть больше 0")
        @Max(value = 5, message = "Оценка товара должна быть меньше 6")
        int score,
        LocalDateTime timestamp,
        List<String> photoIds) {
}