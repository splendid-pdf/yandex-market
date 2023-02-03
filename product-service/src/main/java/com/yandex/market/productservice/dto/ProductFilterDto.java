package com.yandex.market.productservice.dto;

import com.yandex.market.productservice.model.Dimensions;
import com.yandex.market.productservice.model.ProductType;

import java.util.List;
import java.util.Set;

public record ProductFilterDto(

        String name,

        Set<ProductType> productType,

        Set<String> manufacturer,

        String minWidth,

        String maxWidth,

        String minLength,

        String maxLength,

        Double minWeight,

        Double maxWeight,

        Double minRating

) {
}
