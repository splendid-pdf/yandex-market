package com.marketplace.aggregatorservice.dto;

import java.time.LocalDate;
import java.time.LocalTime;


public record ReceiptMethodPreviewDto(
        LocalDate deliveryDate,
        LocalTime deliveryStart,
        LocalTime deliveryEnd) {
}