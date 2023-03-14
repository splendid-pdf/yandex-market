package com.yandex.market.favoritesservice.mapper;

import com.yandex.market.favoritesservice.dto.FavoriteItemResponseDto;
import com.yandex.market.favoritesservice.model.FavoriteItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FavoriteItemMapper {

    FavoriteItemResponseDto toFavoritesResponseDto(FavoriteItem favoriteItem);
}