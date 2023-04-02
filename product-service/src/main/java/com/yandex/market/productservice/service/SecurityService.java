package com.yandex.market.productservice.service;

import com.yandex.market.productservice.model.Product;
import com.yandex.market.productservice.model.ProductCharacteristic;
import com.yandex.market.productservice.model.ProductImage;
import com.yandex.market.productservice.model.ProductSpecialPrice;
import com.yandex.market.productservice.repository.ProductCharacteristicRepository;
import com.yandex.market.productservice.repository.ProductImageRepository;
import com.yandex.market.productservice.repository.ProductRepository;
import com.yandex.market.productservice.repository.ProductSpecialPriceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.yandex.market.productservice.utils.ExceptionMessagesConstants.*;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductSpecialPriceRepository productSpecialPriceRepository;
    private final ProductCharacteristicRepository productCharacteristicRepository;

    public boolean hasAccessToProduct(UUID sellerId, UUID productId) {
        Product product = productRepository
                .findByExternalId(productId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND_ERROR_MESSAGE, productId)));
        return product.getSellerExternalId().equals(sellerId);
    }

    public boolean hasAccessToImage(UUID sellerId, String url) {
        ProductImage productImage = productImageRepository.findByUrl(url)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND_ERROR_MESSAGE, url)));
        return productImage.getProduct().getSellerExternalId().equals(sellerId);
    }

    public boolean hasAccessToSpecialPrice(UUID sellerId, UUID specialPriceId) {
        ProductSpecialPrice specialPrice = productSpecialPriceRepository.findByExternalId(specialPriceId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(SPECIAL_PRICE_NOT_FOUND_ERROR_MESSAGE, specialPriceId)));
        return specialPrice.getProduct().getSellerExternalId().equals(sellerId);
    }

    public boolean hasAccessToCharacteristic(UUID sellerId, UUID characteristicId) {
        ProductCharacteristic characteristic = productCharacteristicRepository.findByExternalId(characteristicId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(PRODUCT_CHARACTERISTIC_NOT_FOUND_ERROR_MESSAGE, characteristicId)));
        return characteristic.getProduct().getSellerExternalId().equals(sellerId);
    }
}
