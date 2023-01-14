package com.yandex.market.shopservice.repositories;

import com.yandex.market.shopservice.dto.projections.BranchResponseProjection;
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

    @Query(value = """
                SELECT
                b as branch,
                ss as shopSystem
                FROM Branch b
                INNER JOIN ShopSystem ss
                ON ss.id = b.shopSystem.id
                WHERE b.shopSystem.externalId=:externalId
            """)
    Page<BranchResponseProjection> findAllByShopSystemExternalId(@Param("externalId") UUID externalId,
                                                                 Pageable pageable);

    @Query(value = """
                SELECT
                b as branch,
                ss as shopSystem
                FROM Branch b
                INNER JOIN ShopSystem ss
                ON ss.id = b.shopSystem.id
                WHERE b.externalId = :externalId
            """)
    Optional<BranchResponseProjection> getBranchResponseByExternalid(@Param("externalId") UUID externalId);
}
