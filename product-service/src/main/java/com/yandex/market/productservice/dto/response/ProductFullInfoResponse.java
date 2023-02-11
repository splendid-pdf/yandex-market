package com.yandex.market.productservice.dto.response;


import com.yandex.market.productservice.model.Dimensions;
import com.yandex.market.productservice.model.ProductType;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductFullInfoResponse(
        UUID externalId,
        String name,
        String description,
        ProductType productType,
        String manufacturer,
        String imageUrl,
        Double weight,
        Dimensions dimensions,
        Boolean isVisible,
        Double rating,
//        List<Characteristic> characteristics,
        UUID branchId,
        double price,
        double discountedPrice,
        LocalDateTime specialPriceFromDate,
        LocalDateTime specialPriceToDate
) {
}


