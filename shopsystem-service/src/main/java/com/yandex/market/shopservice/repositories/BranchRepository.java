package com.yandex.market.shopservice.repositories;

import com.yandex.market.shopservice.model.branch.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    @Query(value = "FROM Branch b " +
            "WHERE b.externalId=:externalId AND b.pickup = false")
    Optional<Branch> findByExternalId(@Param("externalId") UUID externalId);
}
