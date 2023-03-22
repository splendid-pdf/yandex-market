package com.yandex.market.productservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "product_special_prices")
@EqualsAndHashCode(of = "id")
public class ProductSpecialPrice {

    @Id
    @SequenceGenerator(name = "product_special_prices_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_special_prices_sequence")
    private Long id;

    private UUID externalId;

    private LocalDateTime specialPriceFromDate;

    private LocalDateTime specialPriceToDate;

    private Long specialPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    Product product;
}