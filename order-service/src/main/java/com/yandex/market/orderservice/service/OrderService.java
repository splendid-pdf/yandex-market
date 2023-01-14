package com.yandex.market.orderservice.service;

import com.yandex.market.orderservice.dto.OrderDto;
import com.yandex.market.orderservice.mapper.OrderMapper;
import com.yandex.market.orderservice.model.Order;
import com.yandex.market.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    public OrderDto create(OrderDto orderDto, UUID userId) {
        Order order = orderMapper.toOrder(orderDto);
        order.setUserId(userId);
        return orderMapper.toOrderDto(orderRepository.save(order));
    }

    public OrderDto getByExternalId(UUID externalId) {
        Order order = orderRepository.getOrderByExternalId(externalId);
        return orderMapper.toOrderDto(order);
    }

    public List<OrderDto> getOrderByUserId(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> pagedResult = orderRepository.getOrderByUserId(userId, pageable);
        return pagedResult.getContent().stream().map(order -> orderMapper.toOrderDto(order)).collect(Collectors.toList());
    }
}