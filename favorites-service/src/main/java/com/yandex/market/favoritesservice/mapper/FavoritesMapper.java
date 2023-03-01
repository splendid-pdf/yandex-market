package com.yandex.market.favoritesservice.mapper;

import com.yandex.market.favoritesservice.dto.FavoritesRequestDto;
import com.yandex.market.favoritesservice.dto.FavoritesResponseDto;
import com.yandex.market.favoritesservice.model.Favorites;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FavoritesMapper {

    @Mapping(target = "externalId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "additionTimestamp", expression = "java(LocalDateTime.now())")
    Favorites toFavorites(FavoritesRequestDto favoritesRequestDto);

    FavoritesResponseDto toFavoritesResponseDto(Favorites favorites);
}