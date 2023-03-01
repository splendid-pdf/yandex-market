package com.yandex.market.productservice.service.impl;

import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.mapper.ProductMapper;
import com.yandex.market.productservice.model.Product;
import com.yandex.market.productservice.model.VisibleMethod;
import com.yandex.market.productservice.repository.ProductRepository;
import com.yandex.market.productservice.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getPageOfProductsBySellerId(UUID sellerId, Pageable pageable) {
        Page<Product> productsBySellerId = productRepository.getPageOfProductsBySellerId(sellerId, pageable);

        return new PageImpl<>(
                productsBySellerId
                        .stream()
                        .map(productMapper::toResponseDto)
                        .toList()
        );
    }

    @Override
    @Transactional
    public void changeVisibilityForSellerId(UUID sellerId, List<UUID> productIds, VisibleMethod method, boolean methodAction) {
        switch (method) {
            case VISIBLE -> {
                if (methodAction) productRepository.displayProductListForSeller(productIds, sellerId);
                else productRepository.hideProductListForSeller(productIds, sellerId);
            }
            case DELETE -> {
                if (methodAction) productRepository.addListOfGoodsToArchiveForSeller(productIds, sellerId);
                else productRepository.returnListOfGoodsFromArchiveToSeller(productIds, sellerId);
            }
            default -> throw new IllegalArgumentException("Invalid method value: " + method);
        }
    }
}
