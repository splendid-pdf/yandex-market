package com.yandex.market.productservice.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Setter
@Getter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Dimensions {
    private Double weight;

    private Double length;

    private Double width;

    private Double height;
}
