package com.market.shopservice.models.department;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    private String city;

    private String street;

    private String houseNumber;

    private String postcode;

    private double latitude;

    private double longitude;
}
