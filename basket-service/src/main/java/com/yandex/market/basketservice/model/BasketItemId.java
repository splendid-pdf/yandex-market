package com.yandex.market.basketservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class BasketItemId implements Serializable {

    @Column(name = "basket_id")
    private Long basketId;

    @Column(name = "item_id")
    private Long itemId;
}
