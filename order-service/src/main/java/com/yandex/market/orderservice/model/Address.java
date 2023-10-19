package com.yandex.market.orderservice.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@Embeddable
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String deliveryAddress;

    private String entrance;

    private String floor;

    private String apartment;

    private String intercom;

    private String comment;
}