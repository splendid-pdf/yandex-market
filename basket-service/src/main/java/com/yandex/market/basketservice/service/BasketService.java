package com.yandex.market.basketservice.service;

import com.yandex.market.basketservice.dto.ItemRequest;
import com.yandex.market.basketservice.dto.ItemResponse;
import com.yandex.market.basketservice.model.Basket;
import com.yandex.market.basketservice.model.Item;
import com.yandex.market.basketservice.repository.BasketRepository;
import com.yandex.market.basketservice.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.yandex.market.basketservice.utils.ExceptionMessagesConstants.PRODUCT_NOT_FOUND_ERROR_MESSAGE;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public Page<ItemResponse> getAllItemsInsideBasketByUserId(UUID userId, Pageable pageable) {
        return itemRepository.findAllItemsInsideBasketByUserId(userId, pageable);
    }

    public Integer changeItemCountInBasket(UUID userId, @Valid ItemRequest request) {
        Item item = findItemByExternalId(request.productId());
        Basket basket = findBasketByUserId(userId);
        basket.addItem(item, request.numberOfItems());
        basketRepository.save(basket);
        return basket.getAmountItems();
    }

    public Integer deleteItemsList(UUID userId, @NotEmpty List<UUID> itemIds) {

        Basket basket = findBasketByUserId(userId);
        itemIds.forEach(
                externalItemId ->
                        basket.removeItem(
                                Item.builder()
                                        .externalId(externalItemId)
                                        .build()
                        )
        );
        basketRepository.save(basket);
        return basket.getAmountItems();
    }

    private Basket createNewBasket(UUID userId) {
        Basket basket = Basket.builder()
                .externalId(UUID.randomUUID())
                .userId(userId)
                .build();
        return basketRepository.save(basket);
    }

    private Basket findBasketByUserId(UUID userId) {
        Optional<Basket> basketOptional = basketRepository.findBasketByUserId(userId);
        if (basketOptional.isEmpty()) {
            return createNewBasket(userId);
        }
        return basketOptional.get();
    }

    private Item findItemByExternalId(UUID itemId) {
        return itemRepository
                .findProductByExternalId(itemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(
                                PRODUCT_NOT_FOUND_ERROR_MESSAGE,
                                itemId
                        )
                ));
    }
}
