package com.yandex.market.basketservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "baskets")
@EqualsAndHashCode(of = {"id", "externalId", "userId"})
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "external_id")
    private UUID externalId;

    @Column(name = "user_id")
    private UUID userId;

    @OneToMany(
            mappedBy = "basket",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    //check the new sets collections, mb they have method .get() for Set
    private List<BasketItem> items = new ArrayList<>();

    public void addItem(Item item, Integer itemCount) {
        BasketItem basketItem = new BasketItem(this, item, itemCount);
        int indexOfItem = items.indexOf(basketItem);
        if (indexOfItem < 0) {
            items.add(basketItem);
        } else {
            items.get(indexOfItem).setItemCount(itemCount);
        }
    }

    public void removeItem(Item item) {
        for (BasketItem basketItem : items) {
            if (basketItem.getItem().equals(item)) {
                items.remove(basketItem);
                basketItem.setBasket(null);
                basketItem.setItem(null);
                return;
            }
        }
    }
}
