package com.yandex.market.basketservice.service;

import com.yandex.market.basketservice.dto.ItemRequest;
import com.yandex.market.basketservice.dto.ItemResponse;
import com.yandex.market.basketservice.model.Basket;
import com.yandex.market.basketservice.model.BasketItem;
import com.yandex.market.basketservice.model.Item;
import com.yandex.market.basketservice.repository.BasketRepository;
import com.yandex.market.basketservice.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

import static com.yandex.market.basketservice.utils.ExceptionMessagesConstants.PRODUCT_LIST_FOR_REMOVE_IS_NULL;
import static com.yandex.market.basketservice.utils.ExceptionMessagesConstants.PRODUCT_NOT_FOUND_ERROR_MESSAGE;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class BasketService {

    //todo: can i write a message for valid parameters inside methods


    private final BasketRepository basketRepository;
    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public Page<ItemResponse> getAllItemsInsideBasketByUserId(UUID userId, Pageable pageable) {
        return itemRepository.findAllItemsInsideBasketByUserId(userId, pageable);
    }

    public Integer changeItemCountInBasket(UUID userId, @Valid ItemRequest itemRequest) {
        Item item = findItemByExternalId(itemRequest.productId());
        Basket basket = findBasketByUserId(userId);
        basket.addItem(item, itemRequest.numberOfItems());
        basketRepository.save(basket);
        return basket.getAmountItems();
    }

    public Integer deleteItemsList(UUID userId, @NotEmpty List<UUID> itemIdList) {
//        if (itemIdList == null) {
//            throw new NullPointerException(PRODUCT_LIST_FOR_REMOVE_IS_NULL);
//        }

        Basket basket = findBasketByUserId(userId);
        itemIdList.forEach(
                externalItemId ->
                        basket.removeItem(
                                Item
                                        .builder()
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
//        The line below doesn't work and I have no idea why...
//        return basketRepository.findBasketByUserId(userId).orElse(createNewBasket(userId));

        Optional<Basket> basketOptional = basketRepository.findBasketByUserId(userId);
        if(basketOptional.isEmpty()){
            return createNewBasket(userId);
        }
        return basketOptional.get();
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
