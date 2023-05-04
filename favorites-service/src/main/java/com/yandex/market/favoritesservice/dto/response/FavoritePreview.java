package com.yandex.market.favoritesservice.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public interface FavoritePreview {

    UUID getUserId();

    UUID getExternalId();

    LocalDateTime getAddedAt();
}
