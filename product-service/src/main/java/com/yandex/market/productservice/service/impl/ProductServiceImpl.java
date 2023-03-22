package com.yandex.market.productservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.productservice.dto.ProductImageDto;
import com.yandex.market.productservice.dto.ProductSpecialPriceDto;
import com.yandex.market.productservice.dto.ProductUpdateRequestDto;
import com.yandex.market.productservice.dto.projections.ProductPreview;
import com.yandex.market.productservice.dto.projections.SellerArchivePreview;
import com.yandex.market.productservice.dto.projections.SellerProductsPreview;
import com.yandex.market.productservice.dto.request.CreateProductRequest;
import com.yandex.market.productservice.dto.request.ProductCharacteristicUpdateDto;
import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.mapper.ProductCharacteristicMapper;
import com.yandex.market.productservice.mapper.ProductImageMapper;
import com.yandex.market.productservice.mapper.ProductMapper;
import com.yandex.market.productservice.mapper.ProductSpecialPriceMapper;
import com.yandex.market.productservice.metric.dto.ProductMetricsDto;
import com.yandex.market.productservice.metric.enums.UserAction;
import com.yandex.market.productservice.model.Product;
import com.yandex.market.productservice.model.ProductImage;
import com.yandex.market.productservice.model.VisibilityMethod;
import com.yandex.market.productservice.repository.ProductCharacteristicRepository;
import com.yandex.market.productservice.repository.ProductImageRepository;
import com.yandex.market.productservice.repository.ProductRepository;
import com.yandex.market.productservice.repository.ProductSpecialPriceRepository;
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
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;
    private final ProductImageRepository productImageRepository;
    private final ProductSpecialPriceMapper productSpecialPriceMapper;
    private final ProductSpecialPriceRepository productSpecialPriceRepository;
    private final ProductCharacteristicMapper productCharacteristicMapper;
    private final ProductCharacteristicRepository productCharacteristicRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Transactional
    public UUID createProduct(CreateProductRequest createProductRequest, UUID sellerId) {
        Product product = productMapper.toProduct(createProductRequest);
        product.setSellerExternalId(sellerId);
        return productRepository.save(product).getExternalId();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductByExternalId(UUID productId, @Nullable String userId) {
        Product product = findProductByExternalId(productId);

        sendMetricsToKafka(UserAction.VIEW_PRODUCT, product, userId);

        return productMapper.toResponseDto(product);
    }

    @Override
    @Transactional
    public ProductResponseDto updateProductByExternalId(UUID productId, ProductUpdateRequestDto productUpdateRequestDto) {
        Product storedProduct = findProductByExternalId(productId);
        storedProduct = productMapper.toProduct(productUpdateRequestDto, storedProduct);
        return productMapper.toResponseDto(productRepository.save(storedProduct));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsBySetExternalId(Set<UUID> productIdentifiers, Pageable pageable) {
        return productRepository
                .findByExternalId(productIdentifiers, pageable)
                .map(productMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SellerProductsPreview> getProductsBySellerId(UUID sellerId,
                                                             Pageable pageable) {
        return productRepository.findProductsPreviewBySellerId(sellerId, pageable);
    }

    @Override
    public Page<SellerArchivePreview> getArchivedProductsBySellerId(UUID sellerId, Pageable pageable) {
        return productRepository.findArchivedProductsPreviewBySellerId(sellerId, pageable);
    }

    @Override
    @Transactional
    public void changeVisibilityForSellerId(UUID sellerId, List<UUID> productIds,
                                            VisibilityMethod method, boolean methodAction) {
        switch (method) {
            case VISIBLE -> {
                if (methodAction) {
                    productRepository.displayProductsBySellerId(productIds, sellerId);
                } else {
                    productRepository.hideProductsBySellerId(productIds, sellerId);
                }
            }
            case DELETED -> {
                if (methodAction) {
                    productRepository.addProductsToArchiveBySellerId(productIds, sellerId);
                } else {
                    productRepository.returnProductsFromArchiveBySellerId(productIds, sellerId);
                }
            }
            default -> throw new IllegalArgumentException("Некорректный метод = " + method);
        }
    }

    @Override
    @Transactional
    public void deleteFromArchiveListProductBySellerId(List<UUID> productIdentifiers, UUID sellerId) {
        productRepository.deleteProductsBySellerId(productIdentifiers, sellerId);
    }

    @Override
    public Page<ProductPreview> getProductPreviews(Pageable pageable) {
        return productRepository.getProductsPreview(pageable);
    }

    @Override
    public List<ProductPreview> getProductPreviewsByIdentifiers(Set<UUID> productIdentifiers) {
        return productRepository.getProductPreviewsByIdentifiers(productIdentifiers);
    }

    @Transactional
    @Override
    public ProductImageDto addProductImage(UUID productId, ProductImageDto productImageDto) {
        Product product = findProductByExternalId(productId);
        ProductImage productImage = productImageMapper.toProductImage(productImageDto);
        productImageRepository.save(productImage);
        product.addProductImage(productImage);
        return productImageDto;
    }

    @Override
    @Transactional
    public void deleteProductImage(String url) {
        productImageRepository.deleteByUrl(url);
    }

    @Override
    @Transactional
    public UUID addProductSpecialPrice(
            UUID productId,
            ProductSpecialPriceDto productSpecialPriceDto
    ) {
        Product product = findProductByExternalId(productId);
        var productSpecialPrice = productSpecialPriceMapper.toProductSpecialPrice(productSpecialPriceDto);
        product.addProductSpecialPrice(productSpecialPrice);
        return productSpecialPriceRepository.save(productSpecialPrice).getExternalId();
    }

    @Override
    @Transactional
    public ProductSpecialPriceDto updateSpecialPrice(ProductSpecialPriceDto productSpecialPriceDto,
                                                     UUID specialPriceId
    ) {
        var storedProductSpecialPrice = productSpecialPriceRepository.findByExternalId(specialPriceId)
                .orElseThrow(() -> new EntityNotFoundException
                        (PRODUCT_NOT_FOUND_ERROR_MESSAGE + specialPriceId));
        storedProductSpecialPrice =
                productSpecialPriceMapper.toProductSpecialPrice(productSpecialPriceDto, storedProductSpecialPrice);
        productSpecialPriceRepository.save(storedProductSpecialPrice);
        return productSpecialPriceDto;
    }

    @Override
    @Transactional
    public void deleteProductSpecialPrice(UUID specialPriceId) {
        productSpecialPriceRepository.deleteByExternalId(specialPriceId);
    }

    @Override
    public ProductCharacteristicUpdateDto updateProductCharacteristic(
            UUID productCharacteristicExternalId,
            ProductCharacteristicUpdateDto productCharacteristicUpdateDto
    ) {
        var storedProductCharacteristic = productCharacteristicRepository
                .findByExternalId(productCharacteristicExternalId)
                .orElseThrow(() -> new EntityNotFoundException
                        (PRODUCT_NOT_FOUND_ERROR_MESSAGE + productCharacteristicExternalId));
        storedProductCharacteristic = productCharacteristicMapper
                .toProductCharacteristic(productCharacteristicUpdateDto, storedProductCharacteristic);
        productCharacteristicRepository.save(storedProductCharacteristic);
        return productCharacteristicUpdateDto;
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


    private Product findProductByExternalId(UUID productId) {
        return productRepository
                .findByExternalId(productId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND_ERROR_MESSAGE + productId));
    }
}