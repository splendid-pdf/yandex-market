package com.market.shopservice.models.department;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID externalId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private String ogrn;

    @Embedded
    private Country country;

    @Embedded
    private Location location;

    @Embedded
    private Contact contact;

    //TODO openingTimes

    @Embedded
    private Delivery delivery;

    private boolean pickup;

    //TODO features
}
