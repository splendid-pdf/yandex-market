package com.yandex.market.basketservice.repository;

import com.yandex.market.basketservice.model.Basket;
import com.yandex.market.basketservice.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    Optional<Basket> findBasketByUserId(UUID userId);
}
