package com.yandex.market.sellerinfoservice.service;

import com.yandex.market.sellerinfoservice.dto.SellerRequestDto;
import com.yandex.market.sellerinfoservice.model.Seller;

import java.util.UUID;

public interface SellerService {

    UUID createSeller(SellerRequestDto sellerRequestDto);
    
    Seller getSellerByExternalId(UUID sellerExternalId);

    void updateSellerWithDto(UUID sellerId, SellerRequestDto sellerRequestDto);
}