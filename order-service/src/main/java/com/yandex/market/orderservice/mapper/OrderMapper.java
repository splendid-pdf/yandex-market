package com.yandex.market.orderservice.mapper;


import com.yandex.market.orderservice.dto.OrderDto;
import com.yandex.market.orderservice.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toOrderDto(Order order);

    Order toOrder(OrderDto orderDto);
}