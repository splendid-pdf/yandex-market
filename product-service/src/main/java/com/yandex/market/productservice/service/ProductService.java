package com.yandex.market.productservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.productservice.dto.ProductRequestDto;
import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.mapper.ProductMapper;
import com.yandex.market.productservice.metric.dto.ProductMetricsDto;
import com.yandex.market.productservice.metric.enums.UserAction;
import com.yandex.market.productservice.model.Product;
import com.yandex.market.productservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.yandex.market.productservice.utils.ExceptionMessagesConstants.PRODUCT_NOT_FOUND_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @SneakyThrows
    @Transactional
    public UUID createProduct(ProductRequestDto productRequestDto, UUID sellerExternalId) {
        Product product = productMapper.toProduct1(productRequestDto);
        product.setSellerExternalId(sellerExternalId);

        sendMetricsToKafka(UserAction.CREATE_PRODUCT, product);

        return productRepository.save(product).getExternalId();
    }


    @SneakyThrows
    @Transactional(readOnly = true)
    public ProductResponseDto getProductByExternalId(UUID externalId) {
        Product product = productRepository
                .findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND_ERROR_MESSAGE + externalId));

        sendMetricsToKafka(UserAction.VIEW_PRODUCT, product);

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

        sendMetricsToKafka(UserAction.UPDATE_PRODUCT, storedProduct);

        return productMapper.toResponseDto(productRepository.save(storedProduct));
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsBySetExternalId(Set<UUID> externalIdSet, Pageable pageable) {
        return productRepository
                .findByExternalId(externalIdSet, pageable)
                .map(productMapper::toResponseDto)
                .toList();
    }

    @SneakyThrows
    private void sendMetricsToKafka(UserAction userAction, Product product) {
        kafkaTemplate.send(
                "METRICS",
                "product",
                objectMapper.writeValueAsString(
                        ProductMetricsDto.builder()
                                .userAction(userAction)
                                .productExternalId(product.getExternalId())
                                .productName(product.getName())
                                .timestamp(LocalDateTime.now())
                                .build()));
    }

}