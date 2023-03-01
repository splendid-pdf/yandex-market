package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.ProductImageDto;
import com.yandex.market.productservice.model.ProductImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    ProductImage toProductImage(ProductImageDto productImageDto);
    ProductImageDto toProductImageDto(ProductImage productImage);
}
