package com.yandex.market.productservice.repository;

import com.yandex.market.productservice.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TypeRepository extends JpaRepository<Type, Long> {

    Optional<Type> findByName(String name);

    Optional<Type> findByExternalId(UUID typeId);

    @Query("""
            FROM Type t
            LEFT JOIN FETCH t.typeCharacteristics
            """)
    List<Type> findAllFetch();
}
