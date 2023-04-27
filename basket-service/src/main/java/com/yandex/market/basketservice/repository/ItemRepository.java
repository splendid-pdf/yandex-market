package com.yandex.market.basketservice.repository;

import com.yandex.market.basketservice.dto.ItemResponse;
import com.yandex.market.basketservice.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findProductByExternalId(UUID productId);

    @Query(value = """
            SELECT 
                i.external_id AS productId,
                bi.item_count AS totalNumberItemsInBasket
            FROM 
                items AS i 
                JOIN basket_item AS bi ON bi.item_id = i.id
                JOIN baskets AS b ON b.id = bi.basket_id
            WHERE b.user_id =:user_id
            """, nativeQuery = true)
    Page<ItemResponse> findAllItemsInsideBasketByUserId(@Param("user_id") UUID userId, Pageable pageable);
}
