package com.marketplace.aggregatorservice.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Product {

    private Long id;

    private UUID externalId;

    private UUID articleNumber;

    private String name;

    private String description;

    private Boolean isVisible;

    private Boolean isDeleted;

    private UUID sellerExternalId;

    private TaxType taxType;

    private Long price;

    private String articleFromSeller;

    private Long count;

    private Type type;

    private LocalDate creationDate;

    private Set<ProductCharacteristic> productCharacteristics = new HashSet<>();

    private Set<ProductImage> productImages = new HashSet<>();

    private Set<ProductSpecialPrice> productSpecialPrices = new HashSet<>();
}