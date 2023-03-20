package com.yandex.market.productservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_images")
@EqualsAndHashCode(of = "id")
public class ProductImage {

    @Id
    @SequenceGenerator(name = "product_images_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_images_sequence")
    private Long id;
    private String url;
    private boolean isMain;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Product product;
}