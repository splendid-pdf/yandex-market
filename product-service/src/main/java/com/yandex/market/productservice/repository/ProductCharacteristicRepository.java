package com.yandex.market.productservice.repository;

import com.yandex.market.productservice.model.ProductCharacteristic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductCharacteristicRepository extends JpaRepository<ProductCharacteristic, Long> {

    Optional<ProductCharacteristic> findByExternalId(UUID externalId);

}
