package com.yandex.market.productservice.service;

import com.yandex.market.productservice.dto.ProductRequestDto;
import com.yandex.market.productservice.dto.ProductResponseDto;
import com.yandex.market.productservice.mapper.ProductMapper;
import com.yandex.market.productservice.model.Product;
import com.yandex.market.productservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.yandex.market.productservice.utils.ExceptionMessagesConstants.USER_NOT_FOUND_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    public UUID createProduct(ProductRequestDto productRequestDto) {
        Product product = productMapper.toProduct(productRequestDto);
        return productRepository.save(product).getExternalId();
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProductByExternalId(UUID externalId) {
        Product product = productRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_ERROR_MESSAGE + externalId));
        return productMapper.toResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateProductByExternalId(UUID externalId, ProductRequestDto productRequestDto) {
        Product storedProduct = productRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_ERROR_MESSAGE + externalId));
        storedProduct = productMapper.toProduct(productRequestDto, storedProduct);
        return productMapper.toResponseDto(productRepository.save(storedProduct));
    }

    @Transactional
    public void deleteProductByExternalId(UUID externalId) {
        Product product = productRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_ERROR_MESSAGE + externalId));
        product.setIsDeleted(true);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsBySetExternalId(Set<UUID> externalIdSet, Pageable pageable) {
        return productRepository
                .findByExternalId(externalIdSet, pageable)
                .map(productMapper::toResponseDto)
                .toList();
    }
}
