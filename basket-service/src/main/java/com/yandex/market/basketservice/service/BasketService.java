package com.yandex.market.basketservice.service;

import com.yandex.market.basketservice.dto.ItemRequest;
import com.yandex.market.basketservice.dto.ItemResponse;
import com.yandex.market.basketservice.model.Basket;
import com.yandex.market.basketservice.model.Item;
import com.yandex.market.basketservice.repository.BasketRepository;
import com.yandex.market.basketservice.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.yandex.market.basketservice.utils.ExceptionMessagesConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public Page<ItemResponse> getAllItemsInsideBasketByUserId(UUID userId, Pageable pageable) {
        //what will happen if basket doesn't exist?
        return itemRepository.findAllItemsInsideBasketByUserId(userId, pageable);
    }

    public void changeItemCountInBasket(UUID userId, ItemRequest itemRequest) {
        val item = findItemByExternalId(itemRequest.productId());
        var basket = findBasketByUserId(userId);
        basket.addItem(item, itemRequest.numberOfItems());
        basketRepository.save(basket);
    }

    public void deleteItemsList(UUID userId, List<UUID> itemIdList) {
        if (itemIdList == null) {
            throw new NullPointerException(PRODUCT_LIST_FOR_REMOVE_IS_NULL);
        }

        var basket = findBasketByUserId(userId);
        itemIdList.forEach(externalItemId -> basket.removeItem(Item.builder().externalId(externalItemId).build()));
        basketRepository.save(basket);
    }

    private Basket createNewBasket(UUID userId) {
        val basket = Basket.builder()
                .externalId(UUID.randomUUID())
                .userId(userId)
                .build();
        return basketRepository.save(basket);
    }

    private Basket findBasketByUserId(UUID userId) {
        return basketRepository.findBasketByUserId(userId)
                .orElse(createNewBasket(userId));
    }

    private Item findItemByExternalId(UUID eternalIdItem) {
        return itemRepository
                .findProductByExternalId(eternalIdItem)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(
                                PRODUCT_NOT_FOUND_ERROR_MESSAGE,
                                eternalIdItem
                        )
                ));
    }
}
