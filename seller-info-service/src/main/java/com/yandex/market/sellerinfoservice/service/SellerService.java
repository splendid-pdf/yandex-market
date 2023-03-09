package com.yandex.market.sellerinfoservice.service;

import com.yandex.market.sellerinfoservice.dto.SellerRequestDto;
import com.yandex.market.sellerinfoservice.dto.SellerResponseDto;

import java.util.UUID;

public interface SellerService {

    UUID createSeller(SellerRequestDto sellerRequestDto);

    SellerResponseDto getSellerByExternalId(UUID sellerExternalId);

    SellerResponseDto updateSellerWithDto(UUID sellerId, SellerRequestDto sellerRequestDto);

    void deleteSeller(UUID sellerExternalId);
}