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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "favorite_products_seq")
    @SequenceGenerator(name = "favorite_products_seq", allocationSize = 1)
    private Long id;

    private UUID externalId;

    private UUID userId;

    @Builder.Default
    private LocalDateTime addedAt = LocalDateTime.now();
}