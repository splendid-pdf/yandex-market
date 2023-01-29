package com.yandex.market.productservice.service;

import com.yandex.market.productservice.dto.ProductRequestDto;
import com.yandex.market.productservice.dto.ProductResponseDto;
import com.yandex.market.productservice.mapper.ProductMapper;
import com.yandex.market.productservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponseDto getProductByExternalId(UUID externalId) {
        Product product = productRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException("ERROR_MESSAGE" + externalId));
        return productMapper.toResponseDto(product);
    }

    @Transactional
    public void deleteProductByExternalId(UUID externalId) {
        Product product = productRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException("ERROR_MESSAGE" + externalId));
        product.setDeleted(true);
    }

    public String createProduct(ProductRequestDto productRequestDto) {
        Product product = productMapper.toProduct(productRequestDto);
        return productRepository.save(product).getExternalId();
    }

    public ProductResponseDto updateProductByExternalId(UUID externalId, ProductRequestDto productRequestDto) {
        Product storedProduct = productRepository.findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException("ERROR_MESSAGE" + externalId));
        Product updatedProduct = productMapper.toProduct(productRequestDto);
        updatedProduct.setId(storedProduct.getId());
        return productMapper.toResponseDto(productRepository.save(updatedProduct));
    }
}
