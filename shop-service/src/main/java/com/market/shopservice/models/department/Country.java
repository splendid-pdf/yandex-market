package com.market.shopservice.models.department;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    private String countryCode;

    private String countryName;

    private String countryPrefix;

    private String currencyCode;
}
