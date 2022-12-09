package com.market.shopservice.models.shop;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class LegalEntityAddress {

    private String country;

    private String region;

    private String city;

    private String postcode;

    private String street;

    private String houseNumber;

    private String officeNumber;
}
