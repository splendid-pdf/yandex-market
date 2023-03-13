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
@Table(name = "rooms")
@EqualsAndHashCode(of = "id")
public class Room {

    @Id
    @SequenceGenerator(name = "rooms_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rooms_sequence")
    private Long id;

    private UUID externalId;

    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "room_type",
            joinColumns = {@JoinColumn(name = "room_id")},
            inverseJoinColumns = {@JoinColumn(name = "type_id")}
    )
    @Builder.Default
    private Set<Type> types = new HashSet<>();

    public void addType(Type type) {
        type.getRooms().add(this);
        types.add(type);
    }

    public void removeType(Type type) {
        type.getRooms().remove(this);
        types.remove(type);
    }
}