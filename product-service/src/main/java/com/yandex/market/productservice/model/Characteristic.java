package com.yandex.market.productservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "characteristics")
@EqualsAndHashCode(of = "id")
public class Characteristic {

    @Id
    @SequenceGenerator(name = "characteristics_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "characteristics_sequence")
    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private ValueType valueType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Type type;

    public void addType(Type type) {
        type.getCharacteristics().add(this);
       this.type = type;
    }

//    public void removeType(Type type) {
//        type.getRooms().remove(this);
//        types.remove(type);
//    }
}