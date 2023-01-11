package com.yandex.market.shopservice.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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
