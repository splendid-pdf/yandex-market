package com.yandex.market.orderservice.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class PaymentMethod {

    @Enumerated(value = EnumType.STRING)
    private PaymentType paymentType;

    private double orderPrice;
}