package com.yandex.market.basketservice.repository;

import com.yandex.market.basketservice.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    Optional<Basket> findBasketByUserId(UUID userId);
}
