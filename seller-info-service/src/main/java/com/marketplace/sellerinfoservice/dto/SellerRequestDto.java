package com.marketplace.sellerinfoservice.dto;

import com.marketplace.sellerinfoservice.model.BusinessModel;
import com.marketplace.sellerinfoservice.validator.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

/**
 * @param itn  ITN - Идентификационный номер налогоплательщика
 * @param psrn PSRN - Основной государственный регистрационный номер
 * @param bic  BIC - Банковский идентификационный код
 */
@Builder
@Schema(description = "Модель для сохранения \"Продавца\"")
public record SellerRequestDto(


        @Schema(description = "Имя продавца")
        @SellerFirstNameConstraint
        String firstName,

        @SellerLastNameConstraint
        @Schema(description = "Фамилия продавца")
        String lastName,

        @Length(max = 255, message = "адресс не должен превышать 255 символов")
        @Schema(description = "Адрес продавца")
        String legalAddress,
        @Length(max = 255, message = "имя фирмы не должно превышать 255 символов")
        @Schema(description = "Имя фирмы продавца")
        String companyName,

        @ImageUrlConstraint
        @Schema(description = "Фото продавца")
        String imageUrl,

        @Schema(description = "Форма регистрации фирмы продавца")
        BusinessModel businessModel,

        @ItnConstraint
        @Schema(description = "Идентификационный номер налогоплательщика продавца")
        String itn,

        @PsrnConstraint
        @Schema(description = "Основной государственный регистрационный номер продавца")
        String psrn,

        @BicConstraint
        @Schema(description = "Банковский идентификационный код продавца")
        String bic,

        @PaymentAccountConstraint
        @Schema(description = "Банковский расчетный счет продавца")
        String paymentAccount,

        @CorporateAccountConstraint
        @Schema(description = "Банковский корпаративный счет продавца")
        String corporateAccount) {
}