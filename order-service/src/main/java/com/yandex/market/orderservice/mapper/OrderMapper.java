package com.yandex.market.orderservice.mapper;

import com.yandex.market.orderservice.dto.OrderRequest;
import com.yandex.market.orderservice.dto.OrderResponse;
import com.yandex.market.orderservice.dto.OrderResponsePreview;
import com.yandex.market.orderservice.dto.seller.SellerOrderPreview;
import com.yandex.market.orderservice.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderedProductMapper.class, ReceiptMethodMapper.class})
public interface OrderMapper {

    @Mapping(target = "orderStatus", constant = "CREATED")
    @Mapping(target = "externalId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "creationTimestamp", expression = "java(LocalDateTime.now())")
    @Mapping(target = "paid", defaultValue = "true")
    Order toOrder(OrderRequest orderRequest);

    @Mapping(target = "orderNumber", expression = "java('№' + String.valueOf(order.getId()))")
    OrderResponse toOrderResponseDto(Order order);

    @Mapping(source = "order.orderedProducts", target = "orderedProductPreviews")
    @Mapping(target = "orderNumber", expression = "java('№' + String.valueOf(order.getId()))")
    OrderResponsePreview toOrderPreviewDto(Order order);

    @Mapping(target = "sellerOrderContactPreview", source = "order.receiptMethod")
    @Mapping(target = "orderedProductPreviews", source = "order.orderedProducts")
    @Mapping(target = "orderNumber", expression = "java('№' + String.valueOf(order.getId()))")
    SellerOrderPreview toSellerPreview(Order order);
}