package com.yandex.market.shopservice.dto.branch;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record DeliveryIntervalDto(
        @NotBlank(message = "\"Interval ID\" field must not be empty")
        String intervalId,

        @NotNull(message = "\"Start time delivery\" field must not be empty")
        @PastOrPresent(message = "\"Start time delivery\" field must not be future")
        LocalTime periodStart,

        @NotNull(message = "\"End time delivery\" field must not be empty")
        @Future(message = "\"End time delivery\" field must not be past")
        LocalTime periodEnd) {
}
