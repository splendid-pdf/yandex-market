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

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "product_category",
            joinColumns = {@JoinColumn(name = "category_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")}
    )
    private Set<Product> products = new HashSet<>();

    public void addCategoryCharacteristic(CategoryCharacteristic categoryCharacteristic) {
        categoryCharacteristic.setCategory(this);
        categoryCharacteristics.add(categoryCharacteristic);
    }

    public void removeCategoryCharacteristic(CategoryCharacteristic categoryCharacteristic) {
        categoryCharacteristics.remove(categoryCharacteristic);
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }


}
