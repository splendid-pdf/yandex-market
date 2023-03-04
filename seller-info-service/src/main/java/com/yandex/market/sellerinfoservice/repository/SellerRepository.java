package com.yandex.market.sellerinfoservice.repository;

import com.yandex.market.sellerinfoservice.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    boolean existsSellerByEmail(String email);

    Seller getSellerByExternalId(UUID sellerExternalId);
}