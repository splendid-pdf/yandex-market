package com.yandex.market.sellerinfoservice.service;

import com.yandex.market.auth.dto.ClientAuthDetails;
import com.yandex.market.sellerinfoservice.dto.SellerRegistration;
import com.yandex.market.sellerinfoservice.dto.SellerRequestDto;
import com.yandex.market.sellerinfoservice.dto.SellerResponseDto;

import java.util.UUID;

public interface SellerService {

    UUID createSeller(SellerRegistration sellerRegistration);

    SellerResponseDto getSellerByExternalId(UUID sellerExternalId);

    ClientAuthDetails getSellerAuthDetails(String email);

    SellerResponseDto updateSellerWithDto(UUID sellerId, SellerRequestDto sellerRequestDto);

    void deleteSellerByExternalId(UUID sellerExternalId);
}