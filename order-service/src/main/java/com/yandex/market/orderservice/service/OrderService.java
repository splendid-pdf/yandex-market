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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private static final String COMPLETED_OR_CANCELED_ORDER_CAN_NOT_BE_UPDATED = "Completed or canceled orders can not be updated";
    private static final String ORDER_BY_EXTERNAL_ID_IS_NOT_FOUND_MESSAGE = "Order not found by external id = '%s'";
    private static final String USER_BY_EXTERNAL_ID_IS_NOT_FOUND_MESSAGE = "Order not found by user external id = '%s'";

    @Transactional
    public List<UUID> create(List<OrderRequest> orderRequestList, UUID userId) {
        List<UUID> listOrderIds = new ArrayList<>();
        for(OrderRequest orderRequest : orderRequestList){
            Order order = orderMapper.toOrder(orderRequest);
            order.setUserId(userId);
            order.setOrderNumber(generateOrderNumber());
            orderRepository.save(order);
            listOrderIds.add(order.getExternalId());
        }
        return listOrderIds;
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
    public void cancelOrderByUser(UUID orderId) {
        Order order = getOrderById(orderId);

        checkIfOrderIsReceivedAndThrowUOE(order);

        order.setOrderStatus(OrderStatus.CANCELED_BY_USER);
    }

    @Transactional
    public void cancelOrderBySeller(UUID orderId) {
        Order order = getOrderById(orderId);

        checkIfOrderIsReceivedAndThrowUOE(order);

        order.setOrderStatus(OrderStatus.CANCELED_BY_SELLER);
    }

    @Transactional
    public void sendOrder(UUID orderId) {
        Order order = getOrderById(orderId);

        checkIfOrderIsReceivedAndThrowUOE(order);

        order.setOrderStatus(OrderStatus.SENT);
    }

    @Transactional
    public void receivedOrder(UUID orderId) {
        Order order = getOrderById(orderId);

        checkIfOrderIsReceivedAndThrowUOE(order);

        order.setOrderStatus(OrderStatus.RECEIVED);
    }

    @Transactional
    public OrderResponse updateOrderStatus(UUID orderId, OrderStatus orderStatus) {
        Order storedOrder = getOrderById(orderId);
        checkIfOrderIsReceivedAndThrowUOE(storedOrder);
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

    private static void checkIfOrderIsReceivedAndThrowUOE(Order order) {
        if (OrderStatus.RECEIVED == order.getOrderStatus() ||
                OrderStatus.CANCELED_BY_SELLER == order.getOrderStatus() ||
                OrderStatus.CANCELED_BY_USER == order.getOrderStatus()

        ) {
            throw new UnsupportedOperationException(COMPLETED_OR_CANCELED_ORDER_CAN_NOT_BE_UPDATED);
        }
    }

    private static String generateOrderNumber(){
        int random = new Random().nextInt(1_000_000);
        return "ЗАКАЗ " + random + " от " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}