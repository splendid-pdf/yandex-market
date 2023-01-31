package com.yandex.market.shopservice.util;

import com.yandex.market.shopservice.dto.shop.SpecialOfferDto;
import com.yandex.market.shopservice.model.shop.SpecialOffer;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true)
)
public interface SpecialOfferMapper {
    @Mapping(source = "shopSystem.externalId", target = "shopSystem")
    SpecialOfferDto toSpecialOfferDto(SpecialOffer specialOffer);

    @Mapping(source = "shopSystem", target = "shopSystem.externalId")
    SpecialOffer toSpecialOffer(SpecialOfferDto dto);
}
