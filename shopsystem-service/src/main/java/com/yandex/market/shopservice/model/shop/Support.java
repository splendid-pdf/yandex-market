package com.yandex.market.shopservice.model.shop;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Support {
    private String number;
    private String email;
}
