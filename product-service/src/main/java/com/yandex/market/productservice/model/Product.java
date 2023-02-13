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
@Table(name = "products")
@EqualsAndHashCode(of = "id")
public class Product {

    @Id
    @SequenceGenerator(name = "products_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_sequence")
    private Long id;

    private UUID externalId;

    private UUID manufacturerExternalId;

    private UUID sellerExternalId;

    private String articleNumber;

    private String name;

    private String description;

    private Long price;

    private Long count;

    @Enumerated(value = EnumType.STRING)
    private TaxType taxType;

    private String articleFromSeller;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductCharacteristic> productCharacteristics = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> productImages = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductSpecialPrice> productSpecialPrices = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "product_type",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "type_id")}
    )
    private Set<Type> types = new HashSet<>();

    @Embedded
    private Dimensions dimensions;

    private Double rating;

    private Boolean isVisible;

    private Boolean isDeleted;

    public void addProductCharacteristic(ProductCharacteristic characteristic) {
        characteristic.setProduct(this);
        productCharacteristics.add(characteristic);
    }

    public void removeProductCharacteristic(ProductCharacteristic characteristic) {
        productCharacteristics.remove(characteristic);
    }

    public void addProductImage(ProductImage productImage) {
        productImage.setProduct(this);
        productImages.add(productImage);
    }

    public void removeProductImage(ProductImage productImage) {
        productImages.remove(productImage);
    }

    public void addProductSpecialPrice(ProductSpecialPrice productSpecialPrice) {
        productSpecialPrice.setProduct(this);
        productSpecialPrices.add(productSpecialPrice);
    }

    public void removeProductSpecialPrice(ProductSpecialPrice productSpecialPrice) {
        productSpecialPrices.remove(productSpecialPrice);
    }

    public void addType(Type type) {
        types.add(type);
        type.getProducts().add(this);
    }

    public void removeType(Type type) {
        types.remove(type);
        type.getProducts().remove(this);
    }

}
