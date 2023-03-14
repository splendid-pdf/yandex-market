package com.yandex.market.reviewservice.dto;

import com.yandex.market.reviewservice.model.ReviewType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Модель для сохранения \"Отзыва\"")
public record ReviewDto(
        UUID userId,
        UUID productId,
        ReviewType reviewType,
        String advantages,
        String disadvantages,
        String commentary,
        @Min(value = 1, message = "Оценка товара должна быть больше 0")
        @Max(value = 5, message = "Оценка товара должна быть меньше 6")
        int rating,
        LocalDateTime creationTimestamp,

        LocalDateTime updateTimestamp,
        List<String> photoIds) {
}