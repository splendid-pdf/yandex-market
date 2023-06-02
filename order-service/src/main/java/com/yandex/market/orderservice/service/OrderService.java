package com.yandex.market.orderservice.service;

import com.yandex.market.orderservice.dto.OrderRequest;
import com.yandex.market.orderservice.dto.OrderResponse;
import com.yandex.market.orderservice.dto.OrderResponsePreview;
import com.yandex.market.orderservice.dto.seller.SellerOrderPreview;
import com.yandex.market.orderservice.mapper.OrderMapper;
import com.yandex.market.orderservice.model.Order;
import com.yandex.market.orderservice.model.OrderStatus;
import com.yandex.market.orderservice.repository.OrderRepository;
import com.yandex.market.orderservice.utils.OrderReceiptGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private static final String COMPLETED_ORDER_CAN_NOT_BE_UPDATED = "Completed order can not be updated";
    private static final String ORDER_BY_EXTERNAL_ID_IS_NOT_FOUND_MESSAGE = "Order not found by external id = '%s'";
    private static final String USER_BY_EXTERNAL_ID_IS_NOT_FOUND_MESSAGE = "Order not found by user external id = '%s'";

    @Transactional
    public UUID create(OrderRequest orderRequest, UUID userId) {
        Order order = orderMapper.toOrder(orderRequest);
        order.setUserId(userId);
        orderRepository.save(order);
        //todo: create method generate unic order number
        //todo: перебрать все
        return order.getExternalId();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderResponseById(UUID orderId) {
        return orderMapper.toOrderResponseDto(getOrderById(orderId));
    }

    @Transactional(readOnly = true)
    public Page<OrderResponsePreview> getOrdersByUserId(UUID userId, Pageable pageable) {
        Page<Order> pagedResult = orderRepository.getOrderByUserId(userId, pageable);
        if (pagedResult.getContent().isEmpty()) {
            throw new EntityNotFoundException(
                    USER_BY_EXTERNAL_ID_IS_NOT_FOUND_MESSAGE.formatted(userId));
        }
        return new PageImpl<>(pagedResult.getContent()
                .stream()
                .map(orderMapper::toOrderPreviewDto)
                .toList());
    }

    public List<SellerOrderPreview> getOrderPreviewsBySellerId(UUID sellerId) {
        List<Order> ordersBySellerId = orderRepository.getOrdersBySellerId(sellerId);
        return ordersBySellerId.stream()
                .map(orderMapper::toSellerPreview).toList();
    }

    @Transactional
    public void cancelOrder(UUID orderId) {
        Order order = getOrderById(orderId);

        checkIfOrderIsCompletedAndThrowUOE(order);

        order.setOrderStatus(OrderStatus.CANCELED);
    }

//    @Transactional
//    public OrderResponse update(OrderRequest orderRequest, UUID orderId) {
//        Order storedOrder = getOrderById(orderId);
//
//        checkIfOrderIsCompletedAndThrowUOE(storedOrder);
//
//        Order order = orderMapper.toOrder(orderRequest);
//        order.setId(storedOrder.getId());
//        order.setUserId(storedOrder.getUserId());
//        order.setExternalId(storedOrder.getExternalId());
//        for (int i = 0; i < storedOrder.getOrderedProducts().size(); i++) {
//            order.getOrderedProducts().get(i).setId(storedOrder.getOrderedProducts().get(i).getId());
//        }
//
//        orderRepository.save(order);
//        return orderMapper.toOrderResponseDto(order);
//    }

    @Transactional
    public OrderResponse updateOrderStatus(UUID orderId, OrderStatus orderStatus) {
        Order storedOrder = getOrderById(orderId);
        checkIfOrderIsCompletedAndThrowUOE(storedOrder);
        storedOrder.setOrderStatus(orderStatus);
        return orderMapper.toOrderResponseDto(storedOrder);
    }

    @SneakyThrows
    public ByteArrayInputStream createCheck(UUID orderId) {
        return OrderReceiptGenerator.generate(getOrderById(orderId));
    }

    private Order getOrderById(UUID orderId) {
        return orderRepository.getOrderByExternalId(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ORDER_BY_EXTERNAL_ID_IS_NOT_FOUND_MESSAGE.formatted(orderId)));
    }

    private static void checkIfOrderIsCompletedAndThrowUOE(Order order) {
        if (OrderStatus.COMPLETED == order.getOrderStatus()) {
            throw new UnsupportedOperationException(COMPLETED_ORDER_CAN_NOT_BE_UPDATED);
        }
    }
}