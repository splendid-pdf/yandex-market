package com.market.shopservice.models.branch;

import com.market.shopservice.models.shop.Location;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "branches")
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

    @Embedded
    private Contact contact;

    //TODO openingTimes

    @OneToOne(mappedBy = "branch", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Delivery delivery;

    private boolean pickup;

    //TODO features
}
