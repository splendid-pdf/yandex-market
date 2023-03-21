package com.marketplace.aggregatorservice.dto;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ProductImage {

    private Long id;

    private String url;

    private Product product;

}