package com.market.shopservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class Review {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShopSystem shopSystem;

    @Column(nullable = false)
    private Float rating;

    @Column(length = 300)
    private String advantages;

    @Column(length = 300)
    private String disadvantages;

    @Column(length = 300)
    private String summary;

    private boolean anonymous;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id) && Objects.equals(userId, review.userId)
                && Objects.equals(shopSystem, review.shopSystem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, shopSystem);
    }
}
