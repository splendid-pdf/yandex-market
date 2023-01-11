package com.yandex.market.shopservice.model.branch;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class SupportedPaymentMethods {
    private boolean online;
    private boolean cash;
}
