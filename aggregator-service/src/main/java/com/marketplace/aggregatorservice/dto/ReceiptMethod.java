package com.marketplace.aggregatorservice.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptMethod {

    private DeliveryMethod deliveryMethod;

    private Address address;

    private String receiverName;

    private String receiverPhone;

    private String receiverEmail;

    private LocalDate deliveryDate;

    private LocalTime deliveryStart;

    private LocalTime deliveryEnd;

    private double deliveryCost;
}