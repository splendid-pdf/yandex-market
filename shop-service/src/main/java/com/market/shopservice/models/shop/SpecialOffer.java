package com.market.shopservice.models.shop;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "special_offers")
public class SpecialOffer {

    @Id
    @SequenceGenerator(name = "special_offer_seq", sequenceName = "special_offer_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "special_offer_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShopSystem shopSystem;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Integer value;

    @Column(nullable = false)
    private String terms;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecialOffer that = (SpecialOffer) o;
        return id.equals(that.id) && shopSystem.equals(that.shopSystem) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shopSystem, name);
    }
}
