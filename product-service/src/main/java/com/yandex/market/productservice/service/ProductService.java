package com.yandex.market.productservice.service;

import com.yandex.market.productservice.dto.ProductRequestDto;
import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.mapper.ProductMapper;
import com.yandex.market.productservice.model.Product;
import com.yandex.market.productservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.yandex.market.productservice.utils.ExceptionMessagesConstants.PRODUCT_NOT_FOUND_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public UUID createProduct(ProductRequestDto productRequestDto, UUID sellerExternalId) {
        Product product = productMapper.toProduct1(productRequestDto);
        product.setSellerExternalId(sellerExternalId);
        return productRepository.save(product).getExternalId();
    }


    @Transactional(readOnly = true)
    public ProductResponseDto getProductByExternalId(UUID externalId) {
        CompletableFuture<SendResult<String, String>> send = kafkaTemplate.send("METRICS", "product", "hELL");
        Product product = productRepository
                .findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND_ERROR_MESSAGE + externalId));
        return productMapper.toResponseDto(product);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProductNoLimitsByExternalId(UUID externalId) {
        return productMapper.toResponseDto(productRepository
                .findNoLimitsByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND_ERROR_MESSAGE + externalId)));
    }

    @Transactional
    public ProductResponseDto updateProductByExternalId(UUID externalId, ProductRequestDto productRequestDto) {
        Product storedProduct = productRepository
                .findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND_ERROR_MESSAGE + externalId));
        storedProduct = productMapper.toProduct(productRequestDto, storedProduct);
        return productMapper.toResponseDto(productRepository.save(storedProduct));
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsBySetExternalId(Set<UUID> externalIdSet, Pageable pageable) {
        return productRepository
                .findByExternalId(externalIdSet, pageable)
                .map(productMapper::toResponseDto)
                .toList();
    }
}
