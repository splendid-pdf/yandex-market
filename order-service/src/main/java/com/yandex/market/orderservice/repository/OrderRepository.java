package com.yandex.market.orderservice.repository;

import com.yandex.market.orderservice.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> getOrderByExternalId(UUID externalId);

    Page<Order> getOrderByUserId(UUID userId, Pageable pageable);
}