package com.yandex.market.favoritesservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "favorite_products")
public class FavoriteProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "favorite_product_seq")
    @SequenceGenerator(name = "favorite_product_seq", allocationSize = 1)
    private Long id;

    private UUID externalId;

    @ManyToOne(fetch = FetchType.LAZY)
    private FavoriteItem favoriteItem;

    @Builder.Default
    private LocalDateTime addedAt = LocalDateTime.now();
}