package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.ProductRequestDto;
import com.yandex.market.productservice.dto.ProductResponseDto;
import com.yandex.market.productservice.model.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CharacteristicsListMapper.class, DimensionsMapper.class})
public interface ProductMapper {
    ProductResponseDto toResponseDto(Product product);
    @Mapping(target = "externalId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "isDeleted", constant = "false")
    Product toProduct(ProductRequestDto productRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product toProduct(ProductRequestDto productRequestDto, @MappingTarget Product product);
}
