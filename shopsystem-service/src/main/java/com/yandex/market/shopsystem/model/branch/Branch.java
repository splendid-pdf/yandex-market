package com.yandex.market.shopsystem.model.branch;

import com.yandex.market.shopsystem.model.shop.Location;
import com.yandex.market.shopsystem.model.shop.ShopSystem;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "branches")
@EqualsAndHashCode(of = "id")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_seq")
    @SequenceGenerator(name = "branch_seq", sequenceName = "branch_sequence", allocationSize = 1)
    private Long id;

    private UUID externalId;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShopSystem shopSystem;

    private String name;

    private String token;

    private String ogrn;

    @Embedded
    private Location location;

    @Embedded
    private Contact contact;

    //TODO openingTimes

    @OneToOne(mappedBy = "branch", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Delivery delivery;

    private boolean pickup;

    //TODO features
}
