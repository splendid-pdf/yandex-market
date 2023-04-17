package com.yandex.market.basketservice.repository;

import com.yandex.market.basketservice.model.Basket2Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BasketProductRepository extends JpaRepository<Basket2Product, Long> {
    @Query(value = "SELECT * FROM basket_product WHERE basket_id=:basket_id AND product_id=:product_id", nativeQuery = true)
    Optional<Basket2Product> findBasketRepositoryByBasketIdAndProductId(
            @Param("basket_id") Long basketId,
            @Param("product_id") Long productId);

    @Query(value = "SELECT * FROM basket_product WHERE basket_id=:basket_id", nativeQuery = true)
    Page<Basket2Product> findAllProductsByBasket(@Param("basket_id") Long basketId, Pageable pageable);
}
