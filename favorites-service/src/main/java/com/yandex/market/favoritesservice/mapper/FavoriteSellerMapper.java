package com.yandex.market.favoritesservice.mapper;

import com.yandex.market.favoritesservice.dto.request.FavoriteSellerRequest;
import com.yandex.market.favoritesservice.model.FavoriteSeller;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FavoriteSellerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addedAt", ignore = true)
    @Mapping(target = "favoriteItem", ignore = true)
    @Mapping(target = "externalId", source = "sellerId")
    FavoriteSeller toFavoriteSeller(FavoriteSellerRequest favoriteSellerRequest);
}