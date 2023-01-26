package com.yandex.market.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema
public record ReceiptMethodPreviewDto(
        LocalDate deliveryDate,
        LocalTime deliveryStart,
        LocalTime deliveryEnd) {
}