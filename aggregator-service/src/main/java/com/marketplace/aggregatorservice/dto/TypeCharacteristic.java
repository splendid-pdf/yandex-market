package com.marketplace.aggregatorservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class TypeCharacteristic {


    private Long id;

    private String name;

    private ValueType valueType;

    private Type type;
}