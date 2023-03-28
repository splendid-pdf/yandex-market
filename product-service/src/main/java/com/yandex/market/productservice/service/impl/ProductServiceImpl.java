package com.yandex.market.productservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.productservice.dto.ProductImageDto;
import com.yandex.market.productservice.dto.projections.ProductPreview;
import com.yandex.market.productservice.dto.projections.SellerArchivePreview;
import com.yandex.market.productservice.dto.projections.SellerProductPreview;
import com.yandex.market.productservice.dto.request.CreateProductRequest;
import com.yandex.market.productservice.dto.request.ProductCharacteristicRequest;
import com.yandex.market.productservice.dto.request.ProductSpecialPriceRequest;
import com.yandex.market.productservice.dto.request.ProductUpdateRequest;
import com.yandex.market.productservice.dto.response.ProductCharacteristicResponse;
import com.yandex.market.productservice.dto.response.ProductResponse;
import com.yandex.market.productservice.dto.response.ProductSpecialPriceResponse;
import com.yandex.market.productservice.mapper.ProductCharacteristicMapper;
import com.yandex.market.productservice.mapper.ProductImageMapper;
import com.yandex.market.productservice.mapper.ProductMapper;
import com.yandex.market.productservice.mapper.ProductSpecialPriceMapper;
import com.yandex.market.productservice.model.Product;
import com.yandex.market.productservice.model.ProductImage;
import com.yandex.market.productservice.model.Type;
import com.yandex.market.productservice.repository.*;
import com.yandex.market.productservice.service.ProductService;
import com.yandex.market.productservice.service.Validator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.yandex.market.productservice.utils.ExceptionMessagesConstants.*;

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
    private final TypeRepository typeRepository;
    private final ObjectMapper objectMapper;
//    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Validator validator;

    @Override
    @Transactional
    public UUID createProduct(CreateProductRequest createProductRequest, UUID sellerId) {
        UUID typeId = createProductRequest.typeId();
        Type type = typeRepository.findByExternalId(typeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(TYPE_NOT_FOUND_ERROR_MESSAGE, typeId)));
        Product product = productMapper.toProduct(createProductRequest);
        type.addProduct(product);
        product.setSellerExternalId(sellerId);
        validator.validateProductCharacteristics(product);
        return productRepository.save(product).getExternalId();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductByExternalId(UUID productId) {
        Product product = findProductByExternalId(productId);

//        sendMetricsToKafka(UserAction.VIEW_PRODUCT, product, userId);

        return productMapper.toResponseDto(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProductByExternalId(UUID productId, ProductUpdateRequest productUpdateRequest) {
        Product storedProduct = findProductByExternalId(productId);
        storedProduct = productMapper.toProduct(productUpdateRequest, storedProduct);
        return productMapper.toResponseDto(productRepository.save(storedProduct));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsBySetExternalId(Set<UUID> productIds, Pageable pageable) {
        return productRepository
                .findByExternalId(productIds, pageable)
                .map(productMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SellerProductPreview> getProductsBySellerId(UUID sellerId,
                                                            Pageable pageable) {
        return productRepository.findProductsPreviewBySellerId(sellerId, pageable);
    }

    @Override
    @Transactional
    public Page<SellerArchivePreview> getArchivedProductsBySellerId(UUID sellerId, Pageable pageable) {
        return productRepository.findArchivedProductsPreviewBySellerId(sellerId, pageable);
    }

    @Override
    @Transactional
    public void moveProductsFromAndToArchive(UUID sellerId, boolean isArchive, List<UUID> productIds) {
        productRepository.moveProductsFromAndToArchive(sellerId, productIds, isArchive);
    }

    @Override
    @Transactional
    public void changeProductVisibility(UUID sellerId, boolean isVisible, List<UUID> productIds) {
        productRepository.changeProductVisibility(sellerId, productIds, isVisible);
    }

    @Override
    @Transactional
    public void deleteProductsBySellerId(UUID sellerId, List<UUID> productIds) {
        productRepository.deleteProductsBySellerId(sellerId, productIds);
    }

    @Override
    public Page<ProductPreview> getProductPreviews(Pageable pageable) {
        return productRepository.getProductsPreview(pageable);
    }

    @Override
    public List<ProductPreview> getProductPreviewsByIdentifiers(Set<UUID> productIds) {
        return productRepository.getProductPreviewsByIdentifiers(productIds);
    }

    @Override
    @Transactional
    public ProductImageDto addProductImage(UUID productId, ProductImageDto productImageDto) {
        Product product = findProductByExternalId(productId);
        ProductImage productImage = productImageMapper.toProductImage(productImageDto);
        productImageRepository.save(productImage);
        product.addProductImage(productImage);
        return productImageDto;
    }

    @Override
    @Transactional
    public void changeProductPrice(UUID sellerId, UUID productId, Long updatedPrice) {
        productRepository.updateProductPrice(sellerId, productId, updatedPrice);
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
            ProductSpecialPriceRequest productSpecialPriceRequest
    ) {
        Product product = findProductByExternalId(productId);
        var productSpecialPrice = productSpecialPriceMapper.toProductSpecialPrice(productSpecialPriceRequest);
        product.addProductSpecialPrice(productSpecialPrice);
        return productSpecialPriceRepository.save(productSpecialPrice).getExternalId();
    }

    @Override
    @Transactional
    public ProductSpecialPriceResponse updateSpecialPrice(ProductSpecialPriceRequest productSpecialPriceRequest,
                                                          UUID specialPriceId
    ) {
        var storedProductSpecialPrice = productSpecialPriceRepository.findByExternalId(specialPriceId)
                .orElseThrow(() -> new EntityNotFoundException
                        (String.format(SPECIAL_PRICE_NOT_FOUND_ERROR_MESSAGE, specialPriceId)));
        storedProductSpecialPrice =
                productSpecialPriceMapper.toProductSpecialPrice(productSpecialPriceRequest, storedProductSpecialPrice);
        productSpecialPriceRepository.save(storedProductSpecialPrice);
        return productSpecialPriceMapper.toProductSpecialPriceDto(storedProductSpecialPrice);
    }

    @Override
    @Transactional
    public void deleteProductSpecialPrice(UUID specialPriceId) {
        productSpecialPriceRepository.deleteByExternalId(specialPriceId);
    }

    @Override
    public ProductCharacteristicResponse updateProductCharacteristic(
            UUID productCharacteristicExternalId,
            ProductCharacteristicRequest productCharacteristicRequest
    ) {
        var storedProductCharacteristic = productCharacteristicRepository
                .findByExternalId(productCharacteristicExternalId)
                .orElseThrow(() -> new EntityNotFoundException
                        (String.format(PRODUCT_CHARACTERISTIC_NOT_FOUND_ERROR_MESSAGE, productCharacteristicExternalId)));
        storedProductCharacteristic = productCharacteristicMapper
                .toProductCharacteristic(productCharacteristicRequest, storedProductCharacteristic);
        validator.validateProductCharacteristics(storedProductCharacteristic.getProduct());
        productCharacteristicRepository.save(storedProductCharacteristic);
        return productCharacteristicMapper.toProductCharacteristicDto(storedProductCharacteristic);
    }


//    @SneakyThrows
//    private void sendMetricsToKafka(UserAction userAction, Product product, String userId) {
//        kafkaTemplate.send(
//                "METRICS",
//                "product",
//                objectMapper.writeValueAsString(
//                        ProductMetricsDto.builder()
//                                .productExternalId(product.getExternalId())
//                                .userAction(userAction)
//                                .userId(userId)
//                                .productName(product.getName())
//                                .timestamp(LocalDateTime.now())
//                                .build()));
//    }


    private Product findProductByExternalId(UUID productId) {
        return productRepository
                .findByExternalId(productId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND_ERROR_MESSAGE, productId)));
    }
}