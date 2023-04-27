package com.yandex.market.favoritesservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "favorites_sellers")
public class FavoriteSeller {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "favorites_sellers_seq")
    @SequenceGenerator(name = "favorites_sellers_seq", allocationSize = 1)
    private Long id;

    private UUID externalId;

    @ManyToOne(fetch = FetchType.LAZY)
    private FavoriteItem favoriteItem;

    @Builder.Default
    private LocalDateTime addedAt = LocalDateTime.now();
}
