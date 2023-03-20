package com.marketplace.aggregatorservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ProductSpecialPrice {

    private Long id;

    private LocalDateTime specialPriceFromDate;

    private LocalDateTime specialPriceToDate;

    private Long specialPrice;

    Product product;
}