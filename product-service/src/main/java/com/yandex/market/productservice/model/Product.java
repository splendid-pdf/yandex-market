package com.yandex.market.productservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.*;

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

    private String name;

    private String brand;

    private String description;

    private boolean isVisible;

    private boolean isDeleted;

    private boolean isArchived;

    private double rating;

    private UUID sellerExternalId;

    @Enumerated(value = EnumType.STRING)
    private TaxType taxType;

    private Long price;

    private String articleFromSeller;

    private Long count;
    @ManyToOne(fetch = FetchType.LAZY)
    private Type type;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductCharacteristic> productCharacteristics = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductSpecialPrice> productSpecialPrices = new ArrayList<>();

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
}