package com.yandex.market.productservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

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
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Characteristic> characteristics = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "room_type",
            joinColumns = {@JoinColumn(name = "type_id")},
            inverseJoinColumns = {@JoinColumn(name = "room_id")}
    )
    private Set<Room> rooms = new HashSet<>();

    public void addProduct(Product product) {
        product.setType(this);
        products.add(product);
    }

    public void addCharacteristic(Characteristic characteristic) {
        characteristic.setType(this);
        characteristics.add(characteristic);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public void removeTypeCharacteristic(Characteristic characteristic) {
        characteristics.remove(characteristic);
    }

//    public void addRoom(Room room) {
//        room.getTypes().add(this);
//        rooms.add(room);
//    }

    public void removeRoom(Room room) {
        room.getTypes().remove(this);
        rooms.remove(room);
    }
}