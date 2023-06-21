package com.yandex.market.orderservice.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptMethod {

    @Enumerated(value = EnumType.STRING)
    private DeliveryMethod deliveryMethod;

    @Embedded
    private Address address;

    private String receiverFirstName;

    private String receiverLastName;

    private String receiverPhone;

    private String receiverEmail;

    private LocalDate deliveryDate;

    private Long deliveryCost;
}