package com.yandex.market.productservice.dto.projections;

import java.time.LocalDate;
import java.util.UUID;

public interface SellerArchiveProductPreview {

    UUID getId();
    String getName();
    String getSellerId();
    String getBrand();
    Long getPrice();
    Long getCount();
    String getType();
    LocalDate getCreationDate();
    String getImageUrl();

}
