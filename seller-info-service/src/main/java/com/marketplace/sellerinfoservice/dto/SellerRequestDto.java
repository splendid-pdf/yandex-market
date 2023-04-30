package com.marketplace.sellerinfoservice.dto;

import com.marketplace.sellerinfoservice.model.BusinessModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * @param itn  ITN - Идентификационный номер налогоплательщика
 * @param psrn PSRN - Основной государственный регистрационный номер
 * @param bic  BIC - Банковский идентификационный код
 */
@Builder
@Schema(description = "Модель для сохранения \"Продавца\"")
public record SellerRequestDto(

        @Schema(description = "Имя продавца")
        String firstName,
        @Schema(description = "Фамилия продавца")
        String lastName,
        @Schema(description = "Адрес продавца")
        String legalAddress,
        @Schema(description = "Имя фирмы продавца")
        String companyName,
        @Schema(description = "Фото продавца")
        String imageUrl,
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