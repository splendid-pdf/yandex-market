package com.yandex.market.productservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductsTest {
    private String name;

    private UUID id;

    private String brand;

    private String description;

    private boolean isVisible;

    private UUID sellerId;

    private Long price;

    private Long count;

    private String type;

    private String creationDate;
}
