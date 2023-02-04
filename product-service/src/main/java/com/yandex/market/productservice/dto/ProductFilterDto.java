package com.yandex.market.productservice.dto;

import com.yandex.market.productservice.model.ProductType;

import java.util.Set;

public record ProductFilterDto(

        String name,

        Set<ProductType> productTypes,

        Set<String> manufacturers,

        String minWidth,

        String maxWidth,

        String minLength,

        String maxLength,

        String minHeight,

        String maxHeight,

        Double minWeight,

        Double maxWeight,

        Double minRating

) {
}
