package com.yandex.market.productservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
@EqualsAndHashCode(of = "id")
public class Product {

    @Id
    @SequenceGenerator(name = "product-generator", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product-generator")
    private Long id;

    private UUID externalId;

    private Long articleNumber;

    private String name;

    private String description;

    private String productType;

    private String manufacturer;

    private double weight;

    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Characteristics> characteristics;

    @Embedded
    private Dimensions dimensions;

    private Long sortingFactor;

    private Double rating;

    private boolean isVisible;

    private boolean isDeleted;
}
