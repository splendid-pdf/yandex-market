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
@Table(name = "categories")
@EqualsAndHashCode(of = "id")
public class Category {

    @Id
    @SequenceGenerator(name = "category_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_sequence")
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
