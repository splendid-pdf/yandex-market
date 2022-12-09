package com.market.shopservice.models.department;

import com.market.shopservice.models.shop.LegalEntityAddress;
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
@Table(name = "branchs")
public class Branch {

    @Id
    @SequenceGenerator(name = "branch_seq", sequenceName = "branch_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_seq")
    private Long id;

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
    private LegalEntityAddress location;

    @Embedded
    private Contact contact;

    //TODO openingTimes

    @OneToOne(fetch = FetchType.LAZY)
    private Delivery delivery;

    private boolean pickup;

    //TODO features

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch that = (Branch) o;
        return id.equals(that.id) && externalId.equals(that.externalId) && ogrn.equals(that.ogrn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, externalId, ogrn);
    }
}
