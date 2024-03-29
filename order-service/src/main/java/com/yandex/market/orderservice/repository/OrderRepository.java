package com.yandex.market.orderservice.repository;

import com.yandex.market.orderservice.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> getOrderByExternalId(UUID externalId);

    Page<Order> getOrderByUserId(UUID userId, Pageable pageable);

    @Query(value = """
               SELECT o
               FROM Order o
               
               WHERE o.sellerId = :sellerId
                  """)
    List<Order> getOrdersBySellerId(UUID sellerId);
}