package com.yandex.market.shopservice.repositories;

import com.yandex.market.shopservice.model.branch.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    @Query(value = "FROM Branch b " +
            "WHERE b.externalId=:externalId AND b.isDisabled = false")
    Optional<Branch> findByExternalId(@Param("externalId") UUID externalId);

    @Query(value = "FROM Branch b " +
            "WHERE b.shopSystem.externalId=:externalId")
    Page<Branch> findAllByShopSystemExternalId(@Param("externalId") UUID externalId,
                                               Pageable pageable);
}
