package com.marketplace.aggregatorservice.dto;

import java.time.LocalDate;
import java.time.LocalTime;


public record ReceiptMethodRequestDto(

        DeliveryMethod deliveryMethod,
        AddressRequestDto address,
        String receiverName,
        String receiverPhone,
        String receiverEmail,
        LocalDate deliveryDate,
        LocalTime deliveryStart,
        LocalTime deliveryEnd,
        double deliveryCost) {
}