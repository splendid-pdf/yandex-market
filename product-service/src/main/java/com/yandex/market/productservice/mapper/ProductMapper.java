package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.ProductRequestDto;
import com.yandex.market.productservice.dto.ProductResponseDto;
import com.yandex.market.productservice.model.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {CharacteristicsListMapper.class, DimensionsMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true)
)
public interface ProductMapper {

    ProductResponseDto toResponseDto(Product product);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "articleNumber", ignore = true)
    @Mapping(target = "sortingFactor", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "isVisible", defaultValue = "true")
    Product toProduct(ProductRequestDto productRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "articleNumber", ignore = true)
    @Mapping(target = "sortingFactor", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product toProduct(ProductRequestDto productRequestDto, @MappingTarget Product product);

}
