package com.yandex.market.productservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
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

    private UUID articleNumber;

    private String name;

    private String description;

    private Boolean isVisible;

    private Boolean isDeleted;

    private UUID sellerExternalId;

    @Enumerated(value = EnumType.STRING)
    private TaxType taxType;

    private Long price;

    private String articleFromSeller;

    private Long count;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Type type;

    @CreationTimestamp
    private LocalDate creationDate;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductCharacteristic> productCharacteristics = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> productImages = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductSpecialPrice> productSpecialPrices = new HashSet<>();

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