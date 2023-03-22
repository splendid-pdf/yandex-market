package com.yandex.market.productservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_characteristics")
@EqualsAndHashCode(of = "externalId")
public class ProductCharacteristic {

    @Id
    @SequenceGenerator(name = "product_characteristics_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_characteristics_sequence")
    private Long id;

    private String name;

    private UUID externalId;

    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Product product;

    @Enumerated(value = EnumType.STRING)
    private ValueType valueType;
}