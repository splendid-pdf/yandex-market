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

    @Column(unique = true)
    private UUID externalId;

    private UUID userId;

    private UUID productId;

    private LocalDateTime addedAt;
}