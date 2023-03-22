package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.ProductSpecialPriceDto;
import com.yandex.market.productservice.model.Product;
import com.yandex.market.productservice.model.ProductSpecialPrice;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductSpecialPriceMapper {

    @Mapping(target = "externalId", expression = "java(java.util.UUID.randomUUID())")
    ProductSpecialPrice toProductSpecialPrice(ProductSpecialPriceDto productSpecialPriceDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductSpecialPrice toProductSpecialPrice(ProductSpecialPriceDto productSpecialPriceDto,
                                              @MappingTarget ProductSpecialPrice productSpecialPrice);

    ProductSpecialPriceDto toProductSpecialPriceDto(ProductSpecialPrice productSpecialPrice);
}
