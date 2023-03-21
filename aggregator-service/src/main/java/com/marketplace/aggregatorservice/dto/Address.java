package com.marketplace.aggregatorservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String country;

    private String region;

    private String city;

    private String postCode;

    private String street;

    private String houseNumber;

    private String officeNumber;
}