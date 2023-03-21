package com.marketplace.aggregatorservice.dto;

import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Room {

    private Long id;

    private UUID externalId;

    private String name;

    private Set<Type> types = new HashSet<>();

}