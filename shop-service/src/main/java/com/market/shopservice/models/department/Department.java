package com.market.shopservice.models.department;

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
@Table(name = "departments")
public class Department {

    @Id
    @Column(nullable = false)
    @SequenceGenerator(name = "department_seq", sequenceName = "department_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_seq")
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return id.equals(that.id) && externalId.equals(that.externalId) && ogrn.equals(that.ogrn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, externalId, ogrn);
    }
}
