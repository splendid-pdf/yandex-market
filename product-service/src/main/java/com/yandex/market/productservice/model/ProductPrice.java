package com.yandex.market.productservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_prices")
@EqualsAndHashCode(of = "id")
public class ProductPrice {

    @Id
    @SequenceGenerator(name = "productPrice_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productPrice_sequence")
    private Long id;

    private UUID externalId;

    private UUID productId;

    private UUID branchId;

    private UUID shopSystemId;

    private double price;

    private double discountedPrice;

    private LocalDateTime specialPriceFromDate;

    private LocalDateTime specialPriceToDate;
}
