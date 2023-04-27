package com.yandex.market.basketservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"basket", "item"})
@Table(name = "basket_items")
public class BasketItem {
    @EmbeddedId
    private BasketItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("basketId")
    private Basket basket;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("itemId")
    private Item item;

    @Column(name = "item_count")
    private Integer itemCount;

    public BasketItem(Basket basket, Item item, Integer itemCount) {
        this.id = new BasketItemId(basket.getId(), item.getId());
        this.basket = basket;
        this.item = item;
        this.itemCount = itemCount;
    }
}



