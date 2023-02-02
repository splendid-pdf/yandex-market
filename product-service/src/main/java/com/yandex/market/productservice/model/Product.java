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
@Table(name = "products")
@EqualsAndHashCode(of = "id")
public class Product {

    @Id
    @SequenceGenerator(name = "products-generator", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products-generator")
    private Long id;

    private UUID externalId;

    private Long articleNumber;

    private String name;

    private String description;

    @Enumerated(value = EnumType.STRING)
    private ProductType productType;

    private String manufacturer;

    private Double weight;

    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Characteristic> characteristics;

    @Embedded
    private Dimensions dimensions;

    private Long sortingFactor;

    private Double rating;

    private Boolean isVisible;

    private Boolean isDeleted;
}
