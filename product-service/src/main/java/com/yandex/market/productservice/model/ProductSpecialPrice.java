package com.yandex.market.productservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_special_prices")
@EqualsAndHashCode(of = "id")
public class ProductSpecialPrice {

    @Id
    @SequenceGenerator(name = "special_product_prices_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "special_product_prices_sequence")
    private Long id;

    private Long discountedPrice;

    private LocalDateTime specialPriceFromDate;

    private LocalDateTime specialPriceToDate;

    @ManyToOne(fetch = FetchType.LAZY)
    Product product;
}
