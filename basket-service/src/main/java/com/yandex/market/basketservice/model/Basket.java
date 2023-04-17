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
@Table(name  = "baskets")
@EqualsAndHashCode(of = "id")
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private UUID externalId;
    private UUID userId;

    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Basket2Product> basket2Products = new HashSet<>();
}
