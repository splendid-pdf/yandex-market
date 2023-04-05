package com.yandex.market.productservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.productservice.dto.ProductImageDto;
import com.yandex.market.productservice.dto.projections.SellerArchiveProductPreview;
import com.yandex.market.productservice.dto.projections.UserProductPreview;
import com.yandex.market.productservice.dto.projections.SellerProductPreview;
import com.yandex.market.productservice.dto.request.CreateProductRequest;
import com.yandex.market.productservice.dto.request.ProductCharacteristicRequest;
import com.yandex.market.productservice.dto.request.SpecialPriceRequest;
import com.yandex.market.productservice.dto.request.ProductUpdateRequest;
import com.yandex.market.productservice.dto.response.ProductCharacteristicResponse;
import com.yandex.market.productservice.dto.response.ProductResponse;
import com.yandex.market.productservice.dto.response.SpecialPriceResponse;
import com.yandex.market.productservice.mapper.ProductCharacteristicMapper;
import com.yandex.market.productservice.mapper.ProductImageMapper;
import com.yandex.market.productservice.mapper.ProductMapper;
import com.yandex.market.productservice.mapper.ProductSpecialPriceMapper;
import com.yandex.market.productservice.model.Product;
import com.yandex.market.productservice.model.ProductImage;
import com.yandex.market.productservice.model.Type;
import com.yandex.market.productservice.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
public class ProductService {

    private final Validator validator;
    private final ObjectMapper objectMapper;
    private final ProductMapper productMapper;
    private final TypeRepository typeRepository;
    private final ProductRepository productRepository;
    private final ProductImageMapper productImageMapper;
    private final ProductImageRepository productImageRepository;
    private final ProductSpecialPriceMapper productSpecialPriceMapper;
    private final ProductCharacteristicMapper productCharacteristicMapper;
    private final ProductSpecialPriceRepository productSpecialPriceRepository;
    private final ProductCharacteristicRepository productCharacteristicRepository;
//    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public UUID createProduct(CreateProductRequest createProductRequest, UUID sellerId) {
        UUID typeId = createProductRequest.typeId();
        List<ProductImageDto> productImages = createProductRequest.images();

        validator.validateImages(productImages);

        Type type = typeRepository.findByExternalId(typeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(TYPE_NOT_FOUND_ERROR_MESSAGE, typeId)));

        Product product = productMapper.toProduct(createProductRequest);
        type.addProduct(product);
        product.setSellerExternalId(sellerId);
        validator.validateProductCharacteristics(product);

        return productRepository.save(product).getExternalId();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(UUID productId) {
        Product product = findProductByExternalId(productId);
//        sendMetricsToKafka(UserAction.VIEW_PRODUCT, product, userId);
        return productMapper.toResponseDto(product);
    }

    @Transactional
    public ProductResponse updateProductById(UUID productId, ProductUpdateRequest productUpdateRequest) {
        Product storedProduct = findProductByExternalId(productId);
        storedProduct = productMapper.toProduct(productUpdateRequest, storedProduct);
        return productMapper.toResponseDto(productRepository.save(storedProduct));
    }

    @Transactional(readOnly = true)
    public Page<SellerProductPreview> getProductsBySellerId(UUID sellerId, Pageable pageable) {
        return productRepository.findProductsPreviewBySellerId(sellerId, pageable);
    }

    @Transactional
    public void deleteProductsBySellerId(UUID sellerId, List<UUID> productIds) {
        productRepository.deleteProductsBySellerId(sellerId, productIds);
    }

    @Transactional
    public Page<SellerArchiveProductPreview> getArchivedProductsBySellerId(UUID sellerId, Pageable pageable) {
        return productRepository.findArchivedProductsPreviewBySellerId(sellerId, pageable);
    }

    @Transactional
    public void changeIsArchiveField(boolean isArchive, List<UUID> productIds) {
        productRepository.changeIsArchiveField(productIds, isArchive);
    }

    @Transactional
    public void changeProductsVisibility(boolean isVisible, List<UUID> productIds) {
        productRepository.changeProductVisibility(productIds, isVisible);
    }

    public Page<UserProductPreview> getProductPreviews(Pageable pageable) {
        return productRepository.getProductsPreview(pageable);
    }

    public List<UserProductPreview> getProductPreviewsByIds(Set<UUID> productIds) {
        return productRepository.getProductPreviewsByIdentifiers(productIds);
    }

    @Transactional
    public ProductImageDto addProductImage(UUID productId, ProductImageDto productImageDto) {
        Product product = findProductByExternalId(productId);
        ProductImage productImage = productImageMapper.toProductImage(productImageDto);
        productImageRepository.save(productImage);

        if (productImageDto.isMain()) {
            product.getProductImages()
                    .stream()
                    .filter(ProductImage::isMain)
                    .findFirst()
                    .ifPresent(image -> image.setMain(false));
        }
        product.addProductImage(productImage);

        return productImageDto;
    }

    @Transactional
    public void changeProductPrice(UUID productId, Long updatedPrice) {
        productRepository.updateProductPrice(productId, updatedPrice);
    }

    @Transactional
    public void changeProductCount(UUID productId, Long updatedCount) {
        productRepository.updateProductCount(productId, updatedCount);
    }

    @Transactional
    public void deleteProductImage(String url) {
        productImageRepository.deleteByUrl(url);
    }

    @Transactional
    public UUID addProductSpecialPrice(UUID productId, SpecialPriceRequest specialPriceRequest) {
        Product product = findProductByExternalId(productId);
        validator.validateSpecialPrice(specialPriceRequest);
        var productSpecialPrice = productSpecialPriceMapper.toProductSpecialPrice(specialPriceRequest);
        product.addProductSpecialPrice(productSpecialPrice);
        return productSpecialPriceRepository.save(productSpecialPrice).getExternalId();
    }

    @Transactional
    public SpecialPriceResponse updateSpecialPrice(SpecialPriceRequest specialPriceRequest, UUID specialPriceId) {
        validator.validateSpecialPrice(specialPriceRequest);

        var storedProductSpecialPrice = productSpecialPriceRepository.findByExternalId(specialPriceId)
                .orElseThrow(() -> new EntityNotFoundException
                        (String.format(SPECIAL_PRICE_NOT_FOUND_ERROR_MESSAGE, specialPriceId)));

        storedProductSpecialPrice = productSpecialPriceMapper
                .toProductSpecialPrice(specialPriceRequest, storedProductSpecialPrice);
        productSpecialPriceRepository.save(storedProductSpecialPrice);

        return productSpecialPriceMapper.toProductSpecialPriceDto(storedProductSpecialPrice);
    }

    @Transactional
    public void deleteProductSpecialPrice(UUID specialPriceId) {
        productSpecialPriceRepository.deleteByExternalId(specialPriceId);
    }

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