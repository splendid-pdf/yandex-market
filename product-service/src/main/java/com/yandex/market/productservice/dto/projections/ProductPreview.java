package com.yandex.market.productservice.dto.projections;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public interface ProductPreview {

    String getExternalId();

    String getSellerExternalId();

    String getName();

    Long getPrice();

    String getImageUrl();
}