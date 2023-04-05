package com.yandex.market.userservice.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Setter
@Getter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private String city;

    private String deliveryAddress;
}