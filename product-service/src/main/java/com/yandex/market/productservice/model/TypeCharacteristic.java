package com.yandex.market.productservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "type_characteristics")
@EqualsAndHashCode(of = "id")
public class TypeCharacteristic {

    @Id
    @SequenceGenerator(name = "type_characteristics_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "type_characteristics_sequence")
    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private ValueType valueType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Type type;
}
