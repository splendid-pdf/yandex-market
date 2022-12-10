package com.market.shopservice.models.shop;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private String country;
    private String region;
    private String city;
    private String street;
    private String houseNumber;
    private String officeNumber;
    private String postcode;
    private double latitude;
    private double longitude;
}