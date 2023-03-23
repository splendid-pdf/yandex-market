package com.yandex.market.productservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.productservice.dto.ProductImageDto;
import com.yandex.market.productservice.dto.ProductSpecialPriceDto;
import com.yandex.market.productservice.dto.ProductUpdateRequestDto;
import com.yandex.market.productservice.dto.projections.ProductPreview;
import com.yandex.market.productservice.dto.projections.SellerArchivePreview;
import com.yandex.market.productservice.dto.projections.SellerProductPreview;
import com.yandex.market.productservice.dto.request.CreateProductRequest;
import com.yandex.market.productservice.dto.request.ProductCharacteristicUpdateDto;
import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.dto.response.TypeResponse;
import com.yandex.market.productservice.mapper.*;
import com.yandex.market.productservice.model.Product;
import com.yandex.market.productservice.model.ProductImage;
import com.yandex.market.productservice.model.Type;
import com.yandex.market.productservice.repository.*;
import com.yandex.market.productservice.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final TypeRepository typeRepository;
    private final TypeMapper typeMapper;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Transactional
    public UUID createProduct(CreateProductRequest createProductRequest, UUID sellerId) {
        UUID typeId = createProductRequest.typeDto().externalId();
        Type type = typeRepository.findByExternalId(typeId)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Type was not found by external id = %s", typeId)));
        Product product = productMapper.toProduct(createProductRequest);
        type.addProduct(product);
        product.setSellerExternalId(sellerId);
        return productRepository.save(product).getExternalId();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductByExternalId(UUID productId, @Nullable String userId) {
        Product product = findProductByExternalId(productId);

//        sendMetricsToKafka(UserAction.VIEW_PRODUCT, product, userId);

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
    public List<ProductResponseDto> getProductsBySetExternalId(Set<UUID> productIds, Pageable pageable) {
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


    @Override
    public TypeResponse getTypeById(UUID typeId) {
        Type type = typeRepository.findByExternalId(typeId)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Type was not found by external id = %s", typeId)));
        return typeMapper.toTypeResponse(type);
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
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND_ERROR_MESSAGE + productId));
    }
}