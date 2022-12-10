package com.market.shopservice.models.shop;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "special_offers")
public class SpecialOffer {

    @Id
    @SequenceGenerator(name = "special_offer_seq", sequenceName = "special_offer_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "special_offer_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShopSystem shopSystem;
    private String name;
    private String type;
    private Integer value;
    
    private String terms;
}
