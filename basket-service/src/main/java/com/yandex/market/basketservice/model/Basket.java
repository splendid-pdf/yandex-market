package com.yandex.market.basketservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "baskets")
@EqualsAndHashCode(of = "userId")
public class Basket {
    @Id
    @SequenceGenerator(name = "baskets_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "baskets_sequence")
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

    public Integer getAmountItems() {
        Integer amount = 0;
        if (getItems().isEmpty()) {
            return 0;
        }
        for (BasketItem basketItem : getItems()) {
            amount += basketItem.getItemCount();
        }
        return amount;
    }
}
