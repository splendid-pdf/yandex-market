package com.yandex.market.orderservice.repository;

import com.yandex.market.orderservice.model.Order;
import com.yandex.market.orderservice.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Order getOrderByExternalId(UUID externalId);

    Page<Order> getOrderByUserId(UUID userId, Pageable pageable);
}