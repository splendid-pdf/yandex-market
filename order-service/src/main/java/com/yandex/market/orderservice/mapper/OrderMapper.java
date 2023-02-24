package com.yandex.market.orderservice.mapper;

import com.yandex.market.orderservice.dto.OrderPreviewDto;
import com.yandex.market.orderservice.dto.OrderRequestDto;
import com.yandex.market.orderservice.dto.OrderResponseDto;
import com.yandex.market.orderservice.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderedProductMapper.class, ReceiptMethodMapper.class})
public interface OrderMapper {

    @Mapping(target = "orderStatus", constant = "CREATED")
    @Mapping(target = "externalId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "creationTimestamp", expression = "java(LocalDateTime.now())")
    Order toOrder(OrderRequestDto orderRequestDto);

    OrderResponseDto toOrderResponseDto(Order order);

    @Mapping(source = "order.orderedProducts", target = "orderedProductPreviews")
    @Mapping(source = "order.receiptMethod", target = "receiptMethod")
    OrderPreviewDto toOrderPreviewDto(Order order);
}