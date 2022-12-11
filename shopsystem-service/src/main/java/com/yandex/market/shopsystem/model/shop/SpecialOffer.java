package com.yandex.market.shopsystem.model.shop;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "special_offers")
@EqualsAndHashCode(of = "id")
public class SpecialOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "special_offer_seq")
    @SequenceGenerator(name = "special_offer_seq", sequenceName = "special_offer_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShopSystem shopSystem;

    private String name;

    private String type;

    private Integer value;

    private String terms;
}
