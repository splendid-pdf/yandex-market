package com.yandex.market.productservice.dto.projections;

import java.time.LocalDate;
import java.util.UUID;

public interface SellerProductPreview {
    UUID getId();

    String getName();

    Long getPrice();

    Long getCount();

    String getType();

    LocalDate getCreationDate();

    boolean getIsVisible();

    String getImageUrl();
}