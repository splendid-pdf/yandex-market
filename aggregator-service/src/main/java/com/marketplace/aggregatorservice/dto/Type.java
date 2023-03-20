package com.marketplace.aggregatorservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Type {

    private Long id;

    private String name;

    private UUID externalId;

    private Set<Product> products = new HashSet<>();

    private Set<TypeCharacteristic> typeCharacteristics = new HashSet<>();

    private Set<Room> rooms = new HashSet<>();

}