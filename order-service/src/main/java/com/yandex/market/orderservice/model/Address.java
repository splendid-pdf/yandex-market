package com.yandex.market.orderservice.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String country;

    private String region;

    private String city;

    private String postCode;

    private String street;

    private int houseNumber;

    private int officeNumber;
}