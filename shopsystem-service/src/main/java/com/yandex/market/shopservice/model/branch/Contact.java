package com.yandex.market.shopservice.model.branch;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    private String hotlinePhone;

    private String servicePhone;

    private String email;
}