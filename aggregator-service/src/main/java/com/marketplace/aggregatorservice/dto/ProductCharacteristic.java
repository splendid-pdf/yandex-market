package com.marketplace.aggregatorservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ProductCharacteristic {

    private Long id;

    private String name;

    private String value;

    private Product product;

    private ValueType valueType;
}