package com.yandex.market.orderservice.service;

import com.yandex.market.orderservice.dto.OrderPreviewDto;
import com.yandex.market.orderservice.dto.OrderRequestDto;
import com.yandex.market.orderservice.dto.OrderResponseDto;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private static final String COMPLETED_ORDER_CAN_NOT_BE_UPDATED = "Completed order can not be updated";
    private static final String ORDER_BY_EXTERNAL_ID_IS_NOT_FOUND_MESSAGE = "Order not found by external id = '%s'";
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    @Transactional
    public UUID create(OrderRequestDto orderRequestDto, UUID userId) {
        Order order = orderMapper.toOrder(orderRequestDto);
        order.setUserId(userId);
        orderRepository.save(order);
        return order.getExternalId();
    }

    public OrderResponseDto getOrderResponseDtoByExternalId(UUID externalId) {
        return orderMapper.toOrderResponseDto(getOrderByExternalId(externalId));
    }

    @Transactional(readOnly = true)
    public Page<OrderPreviewDto> getOrdersByUserId(UUID userId, Pageable pageable) {
        Page<Order> pagedResult = orderRepository.getOrderByUserId(userId, pageable);
        return new PageImpl<>(pagedResult.getContent()
                .stream()
                .map(orderMapper::toOrderPreviewDto)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void cancelOrder(UUID externalId) {
        Order order = getOrderByExternalId(externalId);

        checkIfOrderIsCompletedAndThrowUOE(order);

        order.setOrderStatus(OrderStatus.CANCELED);
    }

    @Transactional
    public OrderResponseDto update(OrderRequestDto orderRequestDto, UUID externalId) {
        Order storedOrder = getOrderByExternalId(externalId);

        checkIfOrderIsCompletedAndThrowUOE(storedOrder);

        Order order = orderMapper.toOrder(orderRequestDto);
        order.setId(storedOrder.getId());

        return orderMapper.toOrderResponseDto(orderRepository.save(order));
    }

    @SneakyThrows
    public ByteArrayInputStream createCheck(UUID externalId) {
        return OrderReceiptGenerator.generate(getOrderByExternalId(externalId));
    }

    private Order getOrderByExternalId(UUID externalId) {
        return orderRepository.getOrderByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ORDER_BY_EXTERNAL_ID_IS_NOT_FOUND_MESSAGE.formatted(externalId)));
    }

    private static void checkIfOrderIsCompletedAndThrowUOE(Order order) {
        if (OrderStatus.COMPLETED == order.getOrderStatus()) {
            throw new UnsupportedOperationException(COMPLETED_ORDER_CAN_NOT_BE_UPDATED);
        }
    }
}