package com.yandex.market.productservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_characteristics")
@EqualsAndHashCode(of = "id")
public class ProductCharacteristic {

    @Id
    @SequenceGenerator(name = "product_characteristics_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_characteristics_sequence")
    private Long id;

    private String name;

    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Enumerated(value = EnumType.STRING)
    private ValueType valueType;

}
