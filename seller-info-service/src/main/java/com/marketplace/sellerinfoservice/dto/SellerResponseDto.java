package com.marketplace.sellerinfoservice.dto;

import com.marketplace.sellerinfoservice.model.BusinessModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

/**
 * @param itn  ITN - Идентификационный номер налогоплательщика
 * @param psrn PSRN - Основной государственный регистрационный номер
 * @param bic  BIC - Банковский идентификационный код
 */

@Builder
@Schema(description = "Модель для отображения модели \"Seller\"")
public record SellerResponseDto(

        @Schema(description = "Внешний id для продавца")
        UUID externalId,
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
        String imageId,
        @Schema(description = "Форма регистрации фирмы продавца")
        BusinessModel businessModel,
        @Schema(description = "Идентификационный номер налогоплательщика продавца")
        String itn,
        @Schema(description = "Основной государственный регистрационный номер продавца")
        String psrn,
        @Schema(description = "Банковский идентификационный код продавца")
        String bic,
        @Schema(description = "Банковский расчетный счет продавца")
        String paymentAccount,
        @Schema(description = "Банковский корпаративный счет продавца")
        String corporateAccount) {
}