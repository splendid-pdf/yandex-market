package com.yandex.market.productservice.dto.projections;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public interface UserProductPreview {

    String getId();

    String getSellerId();

    String getName();

    Long getPrice();

    String getImageUrl();

}