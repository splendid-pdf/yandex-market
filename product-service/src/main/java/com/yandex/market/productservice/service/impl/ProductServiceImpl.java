package com.yandex.market.productservice.service.impl;

import com.yandex.market.productservice.dto.ProductRequestDto;
import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.mapper.ProductMapper;
import com.yandex.market.productservice.model.DisplayProductMethod;
import com.yandex.market.productservice.model.Product;
import com.yandex.market.productservice.model.VisibleMethod;
import com.yandex.market.productservice.repository.ProductRepository;
import com.yandex.market.productservice.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.yandex.market.productservice.utils.ExceptionMessagesConstants.PRODUCT_NOT_FOUND_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public UUID createProduct(ProductRequestDto productRequestDto, UUID sellerExternalId) {
        Product product = productMapper.toProduct1(productRequestDto);
        product.setSellerExternalId(sellerExternalId);
        return repository.save(product).getExternalId();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductByExternalId(UUID externalId) {
        Product product = repository
                .findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND_ERROR_MESSAGE + externalId));
        return productMapper.toResponseDto(product);
    }

    @Override
    @Transactional
    public ProductResponseDto updateProductByExternalId(UUID externalId, ProductRequestDto productRequestDto) {
        Product storedProduct = repository
                .findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND_ERROR_MESSAGE + externalId));
        storedProduct = productMapper.toProduct(productRequestDto, storedProduct);
        return productMapper.toResponseDto(repository.save(storedProduct));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsBySetExternalId(Set<UUID> externalIdSet, Pageable pageable) {
        return repository
                .findByExternalId(externalIdSet, pageable)
                .map(productMapper::toResponseDto)
                .toList();
    }

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
