package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.ProductSpecialPriceDto;
import com.yandex.market.productservice.model.ProductSpecialPrice;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductSpecialPriceMapper {

    ProductSpecialPrice toProductSpecialPrice(ProductSpecialPriceDto productSpecialPriceDto);
    ProductSpecialPriceDto toProductSpecialPriceDto(ProductSpecialPrice productSpecialPrice);
}
