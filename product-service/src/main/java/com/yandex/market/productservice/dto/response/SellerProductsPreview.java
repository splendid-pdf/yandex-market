package com.yandex.market.productservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.UUID;

public interface SellerProductsPreview {
    UUID getExternalId();

    UUID getSellerExternalId();

    String getArticleNumber();

    String getName();

    Long getPrice();
    Long getCount();

    String getType();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

    LocalDate getCreationDate();

    Boolean getIsVisible();

    Boolean getIsDeleted();

    String getImageUrl();
}