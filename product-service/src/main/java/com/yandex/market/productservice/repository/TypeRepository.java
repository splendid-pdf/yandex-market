package com.yandex.market.productservice.repository;

import com.yandex.market.productservice.dto.projections.TypePreview;
import com.yandex.market.productservice.model.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query(
            value = """
            SELECT
                t.name AS name,
                t.external_id AS id
            FROM
                types AS t
            """,
            nativeQuery = true
    )
    List<TypePreview> findAllTypePreviews(Pageable pageable);

    @Query(
            value = """
            SELECT
                t.name AS name,
                t.external_id AS id
            FROM
                rooms AS r
            INNER JOIN room_type AS rt ON r.id = rt.room_id
            INNER JOIN types as t ON t.id = rt.type_id
            WHERE
            r.external_id=:roomId
            """,
            nativeQuery = true
    )
    List<TypePreview> findAllTypePreviewsByRoomId(@Param("roomId") UUID roomId, Pageable pageable);

}
