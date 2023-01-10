package com.yandex.market.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ProductDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product-details-sequence")
    @SequenceGenerator(name = "product-details-sequence", allocationSize = 1)
    private Long id;

    private UUID productId;

    private String name;

    private double price;

    private String description;

    private String photoUrl;

    private UUID branchId; //конкретный магазин

    private UUID shopSystemId; // организация

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
}