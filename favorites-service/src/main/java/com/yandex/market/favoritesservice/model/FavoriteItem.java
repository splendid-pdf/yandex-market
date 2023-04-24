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
@Table(name = "favorites")
@EqualsAndHashCode(of = "id")
public class FavoriteItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "favorites_sequence")
    @SequenceGenerator(name = "favorites_sequence", allocationSize = 1)
    private Long id;

    @Builder.Default
    @Column(unique = true)
    private UUID externalId = UUID.randomUUID();

    private UUID userId;

    private UUID productId;

    @Builder.Default
    private LocalDateTime addedAt = LocalDateTime.now();
}