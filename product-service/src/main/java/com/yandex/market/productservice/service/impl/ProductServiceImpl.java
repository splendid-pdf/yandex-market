package com.yandex.market.productservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.productservice.dto.ProductRequestDto;
import com.yandex.market.productservice.dto.response.SellerProductsPreview;
import com.yandex.market.productservice.dto.response.ProductPreview;
import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.mapper.ProductMapper;
import com.yandex.market.productservice.metric.dto.ProductMetricsDto;
import com.yandex.market.productservice.metric.enums.UserAction;
import com.yandex.market.productservice.model.DisplayProductMethod;
import com.yandex.market.productservice.model.Product;
import com.yandex.market.productservice.model.VisibilityMethod;
import com.yandex.market.productservice.repository.ProductRepository;
import com.yandex.market.productservice.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
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
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ProductMapper productMapper;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Transactional
    public UUID createProduct(ProductRequestDto productRequestDto, UUID sellerId) {
        Product product = productMapper.toProduct(productRequestDto);
        product.setSellerExternalId(sellerId);
        return repository.save(product).getExternalId();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductByExternalId(UUID externalId, @Nullable String userId) {
        Product product = findProductByExternalId(externalId);

        sendMetricsToKafka(UserAction.VIEW_PRODUCT, product, userId);

        return productMapper.toResponseDto(product);
    }

    @Override
    @Transactional
    public ProductResponseDto updateProductByExternalId(UUID externalId, ProductRequestDto productRequestDto) {
        Product storedProduct = findProductByExternalId(externalId);
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
    public Page<SellerProductsPreview> getPageListOrArchiveBySellerId(UUID sellerId,
                                                                      DisplayProductMethod method,
                                                                      Pageable pageable) {
        return switch (method) {
            case PRODUCT_LIST -> repository.findProductsPreviewPageBySellerId(sellerId, pageable);
            case ARCHIVE -> repository.findArchivePreviewPageBySellerId(sellerId, pageable);
        };
    }

    @Override
    @Transactional
    public void changeVisibilityForSellerId(UUID sellerId, List<UUID> productIds,
                                            VisibilityMethod method, boolean methodAction) {
        switch (method) {
            case VISIBLE -> {
                if (methodAction) {
                    repository.displayProductsBySellerId(productIds, sellerId);
                } else {
                    repository.hideProductsBySellerId(productIds, sellerId);
                }
            }
            case DELETED -> {
                if (methodAction) {
                    repository.addProductsToArchiveBySellerId(productIds, sellerId);
                } else {
                    repository.returnProductsFromArchiveBySellerId(productIds, sellerId);
                }
            }
            default -> throw new IllegalArgumentException("Некорректный метод = " + method);
        }
    }

    @Override
    @Transactional
    public void deleteFromArchiveListProductBySellerId(List<UUID> productIds, UUID sellerId) {
        repository.deleteProductsBySellerId(productIds, sellerId);
    }

    @Override
    public Page<ProductPreview> getProductPreviews(Pageable pageable) {
        return repository.getProductsPreview(pageable);
    }

    @Override
    public List<ProductPreview> getProductPreviewsByIdentifiers(Set<UUID> productIdentifiers) {
        return repository.getProductPreviewsByIdentifiers(productIdentifiers);
    }

    @Override
    @Transactional
    public void changeProductPrice(UUID sellerId, UUID productId, Long newPrice) {
        repository.updateProductPrice(sellerId, productId, newPrice);
    }

    @SneakyThrows
    private void sendMetricsToKafka(UserAction userAction, Product product, String userId) {
        kafkaTemplate.send(
                "METRICS",
                "product",
                objectMapper.writeValueAsString(
                        ProductMetricsDto.builder()
                                .productExternalId(product.getExternalId())
                                .userAction(userAction)
                                .userId(userId)
                                .productName(product.getName())
                                .timestamp(LocalDateTime.now())
                                .build()));
    }


    private Product findProductByExternalId(UUID externalId) {
        return repository
                .findByExternalId(externalId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND_ERROR_MESSAGE + externalId));
    }
}
