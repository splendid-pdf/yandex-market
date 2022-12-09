package com.market.shopservice.models;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class LegalEntityAddress {
    private String country;
    private String region;
    private String city;
    private Integer postcode;
    private String street;
    private Integer houseNumber;
    private Integer officeNumber;

    public LegalEntityAddress(String country, String region, String city, Integer postcode, String street, Integer houseNumber, Integer officeNumber) {
        this.country = country;
        this.region = region;
        this.city = city;
        this.postcode = postcode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.officeNumber = officeNumber;
    }
}
