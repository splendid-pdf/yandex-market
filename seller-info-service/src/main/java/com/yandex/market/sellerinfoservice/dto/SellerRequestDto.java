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
        @Schema(description = "Имя продавца")
        String firstName,
        @Schema(description = "Фамилия продавца")
        String lastName,
        @Schema(description = "Электронная почта продавца")
        String email,
        @Schema(description = "Адрес продавца")
        String legalAddress,
        @Schema(description = "Имя фирмы продавца")
        String companyName,
        @Schema(description = "Фото продавца")
        String imageUrl,
        @Schema(description = "Форма регистрации фирмы продавца")
        BusinessModel businessModel,
        @Schema(description = "Идентификационный номер налогоплательщика продавца")
        String ITN,
        @Schema(description = "Основной государственный регистрационный номер продавца")
        String PSRN,
        @Schema(description = "Банковский идентификационный код продавца")
        String BIC,
        @Schema(description = "Банковский расчетный счет продавца")
        String paymentAccount,
        @Schema(description = "Банковский корпаративный счет продавца")
        String corporateAccount) {
}