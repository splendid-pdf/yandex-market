package com.yandex.market.sellerinfoservice.dto;

import com.yandex.market.sellerinfoservice.model.BusinessModel;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Модель для отображения модели \"Seller\"")
public record SellerResponseDto(
        UUID externalId,
        String firstName,
        String lastName,
        String email,
        String legalAddress,
        String companyName,
        String imageUrl,
        BusinessModel businessModel,
        String ITN,
        String PSRN,
        String BIC,
        String paymentAccount,
        String corporateAccount) {
}