package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.request.ProductPriceRequestDto;
import com.yandex.market.productservice.dto.response.ProductPriceResponseDto;
import com.yandex.market.productservice.model.ProductPrice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductPriceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    ProductPrice toProductPrice(ProductPriceRequestDto dto);

    ProductPriceResponseDto toProductPriceResponseDto(ProductPrice productPrice);
}
