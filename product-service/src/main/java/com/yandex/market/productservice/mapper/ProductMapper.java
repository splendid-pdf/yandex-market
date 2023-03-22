package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.ProductUpdateRequestDto;
import com.yandex.market.productservice.dto.request.CreateProductRequest;
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

    @Mapping(target = "isArchived", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product toProduct(ProductUpdateRequestDto productUpdateRequestDto, @MappingTarget Product product);

    @Mapping(target = "externalId", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "articleNumber", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "isArchived" , defaultValue = "false")
    @Mapping(target = "isVisible", defaultValue = "true")
    @Mapping(source = "productCharacteristicDto", target = "productCharacteristics")
    @Mapping(source = "productImageDto", target = "productImages")
    @Mapping(source = "productSpecialPriceDto", target = "productSpecialPrices")
    @Mapping(source = "typeDto", target = "type")
    Product toProduct(CreateProductRequest createProductRequest);
}