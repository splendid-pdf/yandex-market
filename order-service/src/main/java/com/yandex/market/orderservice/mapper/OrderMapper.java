package com.yandex.market.orderservice.mapper;


import com.yandex.market.orderservice.dto.OrderRequestDto;
import com.yandex.market.orderservice.dto.OrderResponseDto;
import com.yandex.market.orderservice.dto.PageableResponseOrderDto;
import com.yandex.market.orderservice.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderedProductMapper.class, ReceiptMethodMapper.class})
public interface OrderMapper {

    Order toOrder(OrderRequestDto orderRequestDto);

    OrderResponseDto toOrderResponseDto(Order order);

    @Mapping(source = "order.orderedProducts", target = "orderedProductsResponseDto")
    @Mapping(source = "order.receiptMethod", target = "receiptMethodDto")
    PageableResponseOrderDto toPageableResponseOrderDto(Order order);
}