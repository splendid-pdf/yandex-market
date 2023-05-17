package com.yandex.market.basketservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
@EqualsAndHashCode(of = {"externalId"})
public class Item {

    @Id
    @SequenceGenerator(name = "items_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_sequence")
    private Long id;

    @Column(name = "external_id")
    private UUID externalId;
}
