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
@Table(name = "types")
@EqualsAndHashCode(of = "id")
public class Type {

    @Id
    @SequenceGenerator(name = "types_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "types_sequence")
    private Long id;

    private String name;

    private UUID externalId;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "product_type",
            joinColumns = {@JoinColumn(name = "type_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")}
    )
    private Set<Product> products = new HashSet<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

}
