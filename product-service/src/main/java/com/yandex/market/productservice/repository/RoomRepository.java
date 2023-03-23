package com.yandex.market.productservice.repository;

import com.yandex.market.productservice.dto.projections.RoomPreview;
import com.yandex.market.productservice.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByExternalId(UUID externalId);

    @Query(
            value = """
            SELECT
                r.name AS name,
                r.external_id AS externalId
            FROM
                rooms AS r
            WHERE
            r.external_id = :externalId
            """,
            nativeQuery = true
    )
    Optional<RoomPreview> findPreviewByExternalId(@Param("externalId") UUID externalId);

    @Query(
            value = """
            SELECT
                r.name AS name,
                r.external_id AS externalId
            FROM
                rooms AS r
            """,
            nativeQuery = true
    )
    Page<RoomPreview> findAllRoomPreviews(Pageable pageable);

    @Query(
            value = """
            SELECT
                r.name AS name,
                r.external_id AS externalId
            FROM
                rooms AS r
            INNER JOIN room_type AS rt ON r.id = rt.room_id
            INNER JOIN types as t ON t.id = rt.type_id
            WHERE
            t.external_id = :typeId
            """,
            nativeQuery = true
    )
    Page<RoomPreview> findAllRoomPreviewsByTypeId(@Param("typeId") UUID typeId, Pageable pageable);

}
