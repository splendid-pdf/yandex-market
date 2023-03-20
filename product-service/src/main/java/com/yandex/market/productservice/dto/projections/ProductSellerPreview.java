package com.yandex.market.productservice.dto.projections;

import java.time.LocalDate;
import java.util.UUID;

public interface ProductSellerPreview {
    UUID getExternalId();

    UUID getSellerExternalId();

    String getArticleNumber();

    String getName();

    Long getPrice();

    String getType();

    LocalDate getCreationDate();

    Boolean getIsVisible();

    Boolean getIsDeleted();

    String getImageUrl();
}