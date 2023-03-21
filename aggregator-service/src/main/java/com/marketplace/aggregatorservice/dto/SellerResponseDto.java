package com.marketplace.aggregatorservice.dto;

import java.util.UUID;

/**
 * @param ITN  ITN - Идентификационный номер налогоплательщика
 * @param PSRN PSRN - Основной государственный регистрационный номер
 * @param BIC  BIC - Банковский идентификационный код
 */

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