package com.yandex.market.productservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "types")
@EqualsAndHashCode(of = "id")
public class Type {

    @Id
    @SequenceGenerator(name = "types_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "types_sequence")
    private Long id;

    private String name;

    private UUID externalId;

    @OneToMany(mappedBy = "type", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JsonIgnore
    @Builder.Default
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<TypeCharacteristic> typeCharacteristics = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "room_type",
            joinColumns = {@JoinColumn(name = "type_id")},
            inverseJoinColumns = {@JoinColumn(name = "room_id")}
    )
    @Builder.Default
    private Set<Room> rooms = new HashSet<>();

    public void addProduct(Product product) {
        product.setType(this);
        products.add(product);
    }

    public void addCharacteristic(TypeCharacteristic typeCharacteristic) {
        typeCharacteristic.setType(this);
        typeCharacteristics.add(typeCharacteristic);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public void removeTypeCharacteristic(TypeCharacteristic typeCharacteristic) {
        typeCharacteristics.remove(typeCharacteristic);
    }

    public void removeRoom(Room room) {
        room.getTypes().remove(this);
        rooms.remove(room);
    }
}