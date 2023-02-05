package com.yandex.market.orderservice.repository;

import com.yandex.market.orderservice.model.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {

}