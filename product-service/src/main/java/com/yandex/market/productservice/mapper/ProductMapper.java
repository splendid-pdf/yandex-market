package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.ProductRequestDto;
import com.yandex.market.productservice.dto.ProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CharacteristicsListMapper.class)
public interface ProductMapper {
    ProductResponseDto toResponseDto(Product product);
    @Mapping(target = "externalId", expression = "java(UUID.randomUUID())")
    Product toProduct(ProductRequestDto productRequestDto);
}
