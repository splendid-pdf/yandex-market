package com.yandex.market.basketservice.repository;

import com.yandex.market.basketservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findProductByExternalId(UUID productId);
}
