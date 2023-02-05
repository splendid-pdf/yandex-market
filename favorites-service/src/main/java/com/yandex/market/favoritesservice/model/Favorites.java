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
public class Favorites {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "favorites-sequence")
    @SequenceGenerator(name = "favorites-sequence", allocationSize = 1)
    private Long id;

    @Column(unique = true)
    private UUID externalId;

    private UUID productId;

    private UUID userId;

    private LocalDateTime timeStamp;
}