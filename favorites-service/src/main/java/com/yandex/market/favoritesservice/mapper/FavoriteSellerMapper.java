package com.yandex.market.favoritesservice.mapper;

import com.yandex.market.favoritesservice.dto.request.FavoriteSellerDto;
import com.yandex.market.favoritesservice.model.FavoriteSeller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavoriteSellerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addedAt", ignore = true)
    @Mapping(target = "favoriteItem", ignore = true)
    @Mapping(target = "externalId", expression = "sellerId")
    FavoriteSeller toFavoriteSeller(FavoriteSellerDto favoriteSellerDto);
}