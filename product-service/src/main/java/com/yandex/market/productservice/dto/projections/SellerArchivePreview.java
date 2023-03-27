package com.yandex.market.productservice.dto.projections;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SellerArchivePreview {

    UUID getId();

    String getArticleNumber();

    String getName();

    Long getPrice();

    Long getCount();

    String getType();

    LocalDateTime getCreationDate();

    String getImageUrl();
}