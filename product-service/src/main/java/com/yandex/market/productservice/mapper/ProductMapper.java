package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.request.CreateProductRequest;
import com.yandex.market.productservice.dto.request.ProductUpdateRequest;
import com.yandex.market.productservice.dto.response.ProductResponse;
import com.yandex.market.productservice.model.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true),
        uses = {ProductCharacteristicMapper.class, ProductImageMapper.class, ProductSpecialPriceMapper.class, TypeMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductMapper {

    @Mapping(source = "sellerExternalId", target = "sellerId")
    @Mapping(source = "externalId", target = "id")
    @Mapping(source = "productCharacteristics", target = "characteristics")
    @Mapping(source = "productImages", target = "images")
    @Mapping(source = "taxType", target = "tax")
    @Mapping(source = "visible", target = "isVisible")
    @Mapping(source = "archived", target = "isArchived")
    ProductResponse toResponseDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product toProduct(ProductUpdateRequest productUpdateRequest, @MappingTarget Product product);

    @Mapping(target = "externalId", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(source = "characteristics", target = "productCharacteristics")
    @Mapping(source = "images", target = "productImages")
    @Mapping(target = "taxType", expression = "java(TaxType.MAX)")
    @Mapping(target = "visible", constant = "true")
    @Mapping(source = "typeId", target = "type", ignore = true)
    Product toProduct(CreateProductRequest createProductRequest);

}