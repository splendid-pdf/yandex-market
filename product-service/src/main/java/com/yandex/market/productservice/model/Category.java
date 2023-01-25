package com.yandex.market.productservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
@EqualsAndHashCode(of = "id")
public class Category {

    @Id
    @SequenceGenerator(name = "category-generator", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category-generator")
    private Long id;

    private UUID externalId;

    private String name;

    private String description;

    private Long parentId;

    private Long sortingFactor;

    private String imageUrl;

    private boolean isVisible;

    private boolean isDeleted;
}
