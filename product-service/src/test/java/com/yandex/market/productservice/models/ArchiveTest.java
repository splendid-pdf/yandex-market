package com.yandex.market.productservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveTest {
    private UUID id;

    private String name;

    private UUID sellerId;

    private String brand;

    private Long price;

    private Long count;

    private String type;

    private String creationDate;

    private String imageUrl;
}
