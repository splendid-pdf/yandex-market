package com.yandex.market.shopservice.model.shop;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "special_offers")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SpecialOffer {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "special_offer_seq")
    @SequenceGenerator(name = "special_offer_seq", sequenceName = "special_offer_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShopSystem shopSystem;

    private String name;

    @Enumerated(EnumType.STRING)
    private SpecialOfferType type;

    private int value;

    private String terms;

}
