package com.yandex.market.sellerinfoservice.dto;

import com.yandex.market.sellerinfoservice.model.BusinessModel;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @param ITN  ITN - Идентификационный номер налогоплательщика
 * @param PSRN PSRN - Основной государственный регистрационный номер
 * @param BIC  BIC - Банковский идентификационный код
 */

@Schema(description = "Модель для сохранения \"Продавца\"")
public record SellerRequestDto(
        String firstName,
        String lastName,
        String email,
        String legalAddress,
        String companyName,
        BusinessModel businessModel,
        String ITN,
        String PSRN,
        String BIC,
        String paymentAccount,
        String corporateAccount) {
}