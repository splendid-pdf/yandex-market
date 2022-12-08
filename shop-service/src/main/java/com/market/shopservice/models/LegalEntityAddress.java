package com.market.shopservice.models;

import jakarta.persistence.Embeddable;

@Embeddable
public class LegalEntityAddress {
    private String country;
    private String region;
    private String city;
    private Integer postcode;
    private String street;
    private Integer houseNumber;
    private Integer officeNumber;
}
