package com.yandex.market.basketservice.service;

import com.yandex.market.basketservice.dto.ItemRequest;
import com.yandex.market.basketservice.dto.ItemResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@TestPropertySource("/application.yaml")
public class BasketServiceTest {

    private final BasketService basketService;

    @Autowired
    public BasketServiceTest(BasketService basketService) {
        this.basketService = basketService;
    }

    private UUID userId;
    private Pageable pageable;
    private UUID existItemId;
    private Integer numberOfItems;
    private ItemRequest requestExistItem;
    private List<UUID> itemIdRemoveList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        pageable = Pageable.ofSize(2);
        existItemId = UUID.fromString("e9c6a5b3-d85d-4c42-af23-e3c2cd951564");
        numberOfItems = 5;
        requestExistItem = new ItemRequest(existItemId, numberOfItems);
        itemIdRemoveList.add(existItemId);
    }

    @AfterEach
    void tearDown() {
        userId = null;
        pageable = null;
        existItemId = null;
        numberOfItems = null;
        requestExistItem = null;
        itemIdRemoveList = null;
    }

    @Test
    @Transactional
    void addAndDeleteExistItem() {
        basketService.changeItemCountInBasket(userId, requestExistItem);
        Page<ItemResponse> itemResponsePageAfterAdding = basketService.getAllItemsInsideBasketByUserId(userId, pageable);
        Assertions.assertEquals(1, itemResponsePageAfterAdding.getTotalElements());

        ItemResponse itemResponse = itemResponsePageAfterAdding.getContent().get(0);

        Assertions.assertEquals(existItemId, itemResponse.getProductId());
        Assertions.assertEquals(numberOfItems, itemResponse.getTotalNumberItemsInBasket());

        basketService.deleteItemsList(userId, itemIdRemoveList);

        Page<ItemResponse> itemResponsePageAfterDeleting = basketService.getAllItemsInsideBasketByUserId(userId, pageable);
        Assertions.assertEquals(Page.empty(pageable), itemResponsePageAfterDeleting);
    }
}