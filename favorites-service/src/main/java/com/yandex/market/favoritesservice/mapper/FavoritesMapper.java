package com.yandex.market.favoritesservice.mapper;

import com.yandex.market.favoritesservice.dto.FavoritesRequestDto;
import com.yandex.market.favoritesservice.dto.FavoritesResponseDto;
import com.yandex.market.favoritesservice.model.Favorites;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavoritesMapper {

    @Mapping(target = "externalId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "timeStamp", expression = "java(LocalDateTime.now())")
    Favorites toFavorites(FavoritesRequestDto favoritesRequestDto);

    FavoritesResponseDto toFavoritesResponseDto(Favorites favorites);
}