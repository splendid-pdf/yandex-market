package com.yandex.market.orderservice.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptMethod {

    private String address;

    private String receiverName;

    private String receiverPhone;

    private String receiverEmail;

    private LocalDate deliveryDate;

    private LocalTime deliveryStart;

    private LocalTime deliveryEnd;

    private double deliveryCost;
}