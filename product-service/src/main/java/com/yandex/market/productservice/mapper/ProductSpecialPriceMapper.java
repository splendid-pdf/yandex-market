package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.request.SpecialPriceRequest;
import com.yandex.market.productservice.dto.response.SpecialPriceResponse;
import com.yandex.market.productservice.model.ProductSpecialPrice;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductSpecialPriceMapper {

    @Mapping(target = "externalId", expression = "java(java.util.UUID.randomUUID())")
    ProductSpecialPrice toProductSpecialPrice(SpecialPriceRequest specialPriceRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductSpecialPrice toProductSpecialPrice(SpecialPriceRequest specialPriceRequest,
                                              @MappingTarget ProductSpecialPrice productSpecialPrice);

    @Mapping(target = "id", source = "externalId")
    SpecialPriceResponse toProductSpecialPriceDto(ProductSpecialPrice productSpecialPrice);

}