package com.yandex.market.productservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
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
    @SequenceGenerator(name = "categories_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_sequence")
    private Long id;

    private UUID externalId;

    private String name;

    private UUID parentCategoryExternalId;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CategoryCharacteristic> categoryCharacteristics = new HashSet<>();

    @OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Product> products = new HashSet<>();

    public void addCategoryCharacteristic(CategoryCharacteristic categoryCharacteristic) {
        categoryCharacteristic.setCategory(this);
        categoryCharacteristics.add(categoryCharacteristic);
    }

    public void removeCategoryCharacteristic(CategoryCharacteristic categoryCharacteristic) {
        categoryCharacteristics.remove(categoryCharacteristic);
    }

}
