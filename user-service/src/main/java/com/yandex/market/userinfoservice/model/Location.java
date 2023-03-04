package com.yandex.market.userinfoservice.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Setter
@Getter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private String country;
    private String region;
    private String city;
    private String postcode;
    private String street;
    private String houseNumber;
    private String apartmentNumber;
    private Double latitude;
    private Double longitude;
}
