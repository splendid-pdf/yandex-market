package com.market.shopservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SpecialOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private OfferType type;
    private Integer value;
    private String terms;

    // TODO: доделать
//    @ManyToOne
//    @JoinColumn(name = "shop_system_id")
//    private ShopSystem shopSystem;
}
