package com.yandex.market.basketservice.service;

import com.yandex.market.basketservice.dto.ProductRequestDto;
import com.yandex.market.basketservice.dto.ProductResponseDto;
import com.yandex.market.basketservice.errors.NotEnoughIGoodsInStockException;
import com.yandex.market.basketservice.mapper.ProductResponseMapper;
import com.yandex.market.basketservice.model.Basket;
import com.yandex.market.basketservice.model.Basket2Product;
import com.yandex.market.basketservice.repository.BasketProductRepository;
import com.yandex.market.basketservice.repository.BasketRepository;
import com.yandex.market.basketservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.yandex.market.basketservice.utils.ExceptionMessagesConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final BasketProductRepository basketProductRepository;
    private final ProductResponseMapper productResponseMapper;

    public Page<ProductResponseDto> getAllProducts(UUID userId, Pageable pageable) {
        val basket = findBasketByUserId(userId);

        val basketProductList = basketProductRepository
                .findAllProductsByBasket(basket.getId(), pageable).stream()
                .map(productResponseMapper::map).toList();

        //todo: what will happen if .findAllProductByBasket returns empty page?
        return new PageImpl<>(basketProductList);
    }

    public Integer changeNumberOfProductsInBasket(UUID userId, ProductRequestDto productRequestDto) throws Exception {
        val product = productRepository
                .findProductByExternalId(productRequestDto.productId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(
                                PRODUCT_NOT_FOUND_ERROR_MESSAGE,
                                productRequestDto.productId()
                        )
                ));

        if (product.getAvailableAmountOfItems() < productRequestDto.numberOfItems()) {
            throw new NotEnoughIGoodsInStockException(
                    String.format(
                            PRODUCT_NOT_ENOUGH_GOODS_IN_STOCK_ERROR_MESSAGE,
                            product.getExternalId()
                    ));
        }

        var basket = findBasketByUserId(userId);

        var basketProduct = basketProductRepository
                .findBasketRepositoryByBasketIdAndProductId(basket.getId(), product.getId())
                .orElse(
                        Basket2Product.builder()
                                .basket(basket)
                                .product(product)
                                .totalNumberItemsInBasket(productRequestDto.numberOfItems())
                                .build()
                );

        basketProduct.setTotalNumberItemsInBasket(productRequestDto.numberOfItems());

        basketProductRepository.save(basketProduct);

        return basket.getBasket2Products().size() + 1;
    }

    public void deleteProductsSet(UUID userId, Set<UUID> productIdsSet) {
        var basket = findBasketByUserId(userId);

        for (UUID productId : productIdsSet) {
            basket.getBasket2Products()
                    .removeIf(basketProduct -> basketProduct.getProduct().getExternalId() == productId);
        }
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

//    public BasketResponseDto getBasket(UUID userId) {
//        val basket = basketRepository.findBasketByUserId(userId)
//                .orElse(createNewBasket(userId));
//        return basketResponseMapper.map(basket);
//    }
}
