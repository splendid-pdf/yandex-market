package com.yandex.market.productservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public interface ProductPreview {

    String getExternalId();

    String getSellerExternalId();

    String getName();

    Long getPrice();

    String getImageUrl();
}