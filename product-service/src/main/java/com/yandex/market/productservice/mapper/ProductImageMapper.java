package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.ProductImageDto;
import com.yandex.market.productservice.model.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductImageMapper {

    ProductImage toProductImage(ProductImageDto productImageDto);
    ProductImageDto toProductImageDto(ProductImage productImage);
}
