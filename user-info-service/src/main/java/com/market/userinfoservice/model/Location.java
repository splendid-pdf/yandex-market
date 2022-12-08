package com.market.userinfoservice.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Location {
    private String country;
    private String region;
    private String city;
    private String postcode;
    private String street;
    private String houseNumber;
    private String apartmentNumber;
}
