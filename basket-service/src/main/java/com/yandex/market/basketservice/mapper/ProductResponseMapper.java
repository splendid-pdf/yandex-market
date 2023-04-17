package com.yandex.market.basketservice.mapper;

import com.yandex.market.basketservice.dto.ProductResponseDto;
import com.yandex.market.basketservice.model.Basket2Product;
import com.yandex.market.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductResponseMapper implements Mapper<Basket2Product, ProductResponseDto> {

    @Override
    public ProductResponseDto map(Basket2Product basket2Product) {
        return new ProductResponseDto(
                basket2Product.getProduct().getExternalId(),
                basket2Product.getTotalNumberItemsInBasket()
        );
    }
}
