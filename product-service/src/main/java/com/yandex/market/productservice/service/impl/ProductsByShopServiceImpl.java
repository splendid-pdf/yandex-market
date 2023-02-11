package com.yandex.market.productservice.service.impl;

import com.yandex.market.productservice.dto.request.ProductPriceRequestDto;
import com.yandex.market.productservice.dto.response.ProductFullInfoResponse;
import com.yandex.market.productservice.dto.response.ProductPriceResponseDto;
import com.yandex.market.productservice.mapper.ProductPriceMapper;
import com.yandex.market.productservice.model.ProductPrice;
import com.yandex.market.productservice.repository.ProductPriceRepository;
import com.yandex.market.productservice.service.ProductsByShopService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProductsByShopServiceImpl implements ProductsByShopService {

    private final ProductPriceRepository repository;

    private final ProductPriceMapper productPriceMapper;

    private final String PRODUCT_PRICE_NOT_FOUND = "Product price not found by external id = '%s'";

    @Override
    @Transactional
    public Page<ProductFullInfoResponse> getProductsByShop(UUID shopId, Pageable pageable) {
        return new PageImpl<>(
                repository.getPageProductsByShopSystemId(shopId, pageable)
                        .getContent()
                        .stream()
                        .toList());
    }

    @Override
    @Transactional
    public Page<ProductFullInfoResponse> getProductsByBranch(UUID branchId, Pageable pageable) {
        return new PageImpl<>(
                repository.getPageProductsByBranchId(branchId, pageable)
                        .getContent()
                        .stream()
                        .toList());
    }

    @Override
    public ProductPriceResponseDto getProductsByExternalId(UUID externalId) {
        return productPriceMapper.toProductPriceResponseDto(
                repository.findByExternalId(externalId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                PRODUCT_PRICE_NOT_FOUND.formatted(externalId)))
        );
    }

    @Override
    @Transactional
    public UUID createProductPrice(ProductPriceRequestDto dto) {
        ProductPrice productPrice = productPriceMapper.toProductPrice(dto);
        productPrice.setExternalId(UUID.randomUUID());
        repository.save(productPrice);
        return productPrice.getExternalId();
    }
}
