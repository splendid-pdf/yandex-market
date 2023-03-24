package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.request.ProductSpecialPriceRequest;
import com.yandex.market.productservice.dto.response.ProductSpecialPriceResponse;
import com.yandex.market.productservice.model.ProductSpecialPrice;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductSpecialPriceMapper {

    @Mapping(target = "externalId", expression = "java(java.util.UUID.randomUUID())")
    ProductSpecialPrice toProductSpecialPrice(ProductSpecialPriceRequest productSpecialPriceRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductSpecialPrice toProductSpecialPrice(ProductSpecialPriceRequest productSpecialPriceRequest,
                                              @MappingTarget ProductSpecialPrice productSpecialPrice);

    @Mapping(target = "id", source = "externalId")
    ProductSpecialPriceResponse toProductSpecialPriceDto(ProductSpecialPrice productSpecialPrice);
}