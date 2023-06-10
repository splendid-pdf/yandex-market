package com.yandex.market.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ordered_products")
@EqualsAndHashCode(of = "id")
public class OrderedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ordered-product-sequence")
    @SequenceGenerator(name = "ordered-product-sequence", allocationSize = 1)
    private Long id;

    private UUID productId;

    private String articleFromSeller;

    private int amount;

    private String name;

    private Long price;

    private String description;

    private String photoUrl;
}