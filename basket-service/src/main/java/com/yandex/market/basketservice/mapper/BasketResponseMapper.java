package com.yandex.market.basketservice.mapper;

import com.yandex.market.basketservice.dto.BasketResponseDto;
import com.yandex.market.basketservice.dto.ProductResponseDto;
import com.yandex.market.basketservice.model.Basket;
import com.yandex.market.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BasketResponseMapper implements Mapper<Basket, BasketResponseDto> {

    private final ProductResponseMapper productResponseMapper;

    @Override
    public BasketResponseDto map(Basket basket) {
        Set<ProductResponseDto> productSet =
                basket.getBasket2Products().stream()
                        .map(productResponseMapper::map)
                        .collect(Collectors.toSet());

        return new BasketResponseDto(
                basket.getExternalId(),
                basket.getUserId(),
                productSet
                );
    }
}
