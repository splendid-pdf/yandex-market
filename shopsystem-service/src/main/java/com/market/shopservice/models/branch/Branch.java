package com.market.shopservice.models.branch;

import com.market.shopservice.models.shop.Location;
import com.market.shopservice.models.shop.ShopSystem;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "branches")
@EqualsAndHashCode(of = {"externalId"})
public class Branch {

    @Id
    @SequenceGenerator(name = "branch_seq", sequenceName = "branch_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_seq")
    private Long id;
    private UUID externalId;
    private String name;
    private String token;
    private String ogrn;

    @Embedded
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShopSystem shopSystem;

    @Embedded
    private Contact contact;

    //TODO openingTimes

    @OneToOne(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private Delivery delivery;

    private boolean pickup;

    //TODO features
}
