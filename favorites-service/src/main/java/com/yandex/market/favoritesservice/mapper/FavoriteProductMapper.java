package com.yandex.market.favoritesservice.mapper;


import com.yandex.market.favoritesservice.dto.request.FavoriteProductDto;
import com.yandex.market.favoritesservice.model.FavoriteProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavoriteProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addedAt", ignore = true)
    @Mapping(target = "favoriteItem", ignore = true)
    @Mapping(target = "externalId", expression = "productId")
    FavoriteProduct toFavoriteProduct(FavoriteProductDto favoriteProductDto);
}
