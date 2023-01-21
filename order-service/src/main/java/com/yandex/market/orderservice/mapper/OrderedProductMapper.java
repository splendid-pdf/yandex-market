package com.yandex.market.orderservice.mapper;

import com.yandex.market.orderservice.dto.OrderedProductResponseDto;
import com.yandex.market.orderservice.model.OrderedProduct;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderedProductMapper {

    OrderedProductResponseDto toOrderProductResponseDto(OrderedProduct orderedProduct);
}