package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.ProductRequestDto;
import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.model.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true),
        uses = {ProductCharacteristicMapper.class, ProductImageMapper.class, ProductSpecialPriceMapper.class, TypeMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductMapper {

    ProductResponseDto toResponseDto(Product product);

    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "externalId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "articleNumber", expression = "java(UUID.randomUUID())")
    @Mapping(source = "productRequestDto.productCharacteristicDto", target = "productCharacteristics")
    @Mapping(source = "productRequestDto.productImageDto", target = "productImages")
    @Mapping(source = "productRequestDto.productSpecialPriceDto", target = "productSpecialPrices")
    @Mapping(source = "productRequestDto.typeDto", target = "type")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product toProduct(ProductRequestDto productRequestDto, @MappingTarget Product product);

    @Mapping(target = "externalId", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "articleNumber", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(source = "productRequestDto.productCharacteristicDto", target = "productCharacteristics")
    @Mapping(source = "productRequestDto.productImageDto", target = "productImages")
    @Mapping(source = "productRequestDto.productSpecialPriceDto", target = "productSpecialPrices")
    @Mapping(source = "productRequestDto.typeDto", target = "type")
    Product toProduct1(ProductRequestDto productRequestDto);
}