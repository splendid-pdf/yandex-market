package com.yandex.market.productservice.service.impl;

import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.mapper.ProductMapper;
import com.yandex.market.productservice.model.DisplayProductMethod;
import com.yandex.market.productservice.model.Product;
import com.yandex.market.productservice.model.VisibleMethod;
import com.yandex.market.productservice.repository.SellerRepository;
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

    private final SellerRepository repository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getPageListOrArchiveBySellerId(UUID sellerId, DisplayProductMethod method, Pageable pageable) {
        Page<Product> productsBySellerId = switch (method) {
            case PRODUCT_LIST -> repository.getPageOfProductsBySellerId(sellerId, pageable);
            case ARCHIVE -> repository.getArchivePageOfProductsBySellerId(sellerId, pageable);
        };

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
                if (methodAction) repository.displayProductListForSeller(productIds, sellerId);
                else repository.hideProductListForSeller(productIds, sellerId);
            }
            case DELETE -> {
                if (methodAction) repository.addListOfGoodsToArchiveForSeller(productIds, sellerId);
                else repository.returnListOfGoodsFromArchiveToSeller(productIds, sellerId);
            }
        }
    }

    @Override
    @Transactional
    public void deleteFromArchiveListProductBySellerId(List<UUID> productIds, UUID sellerId) {
        repository.deleteProductsBySellerId(productIds, sellerId);
    }
}
