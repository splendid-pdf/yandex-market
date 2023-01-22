package com.yandex.market.orderservice.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReceiptMethodResponseDto(LocalDate deliveryDate, LocalTime deliveryStart, LocalTime deliveryEnd) {
}
