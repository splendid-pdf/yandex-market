package com.yandex.market.sellerinfoservice.repository;

import com.yandex.market.sellerinfoservice.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    boolean existsSellerByEmail(String email);

    @Query(value = """
            FROM Seller seller
            WHERE seller.externalId=:sellerExternalId
            """)
    Optional<Seller> getSellerByExternalId(UUID sellerExternalId);
}