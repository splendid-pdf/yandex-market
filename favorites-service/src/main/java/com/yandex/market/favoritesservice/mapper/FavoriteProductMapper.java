package com.yandex.market.favoritesservice.mapper;

import com.yandex.market.favoritesservice.dto.request.FavoriteProductRequest;
import com.yandex.market.favoritesservice.model.FavoriteProduct;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FavoriteProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "addedAt", ignore = true)
    @Mapping(target = "externalId", source = "productId")
    FavoriteProduct toFavoriteProduct(FavoriteProductRequest favoriteProductRequest);
}