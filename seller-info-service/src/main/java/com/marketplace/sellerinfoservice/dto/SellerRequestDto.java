package com.marketplace.sellerinfoservice.dto;

import com.marketplace.sellerinfoservice.model.BusinessModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
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
        @Pattern(regexp = "[а-яА-Яa-zA-Z]{2,100}",
                message = "Поле имя должно содержать не менее 2 и не более 100 букв")
        String firstName,

        @Schema(description = "Фамилия продавца")
        @Pattern(regexp = "[а-яА-Яa-zA-Z]{2,100}",
                message = "Поле фамилия должно содержать не менее 2 и не более 100 букв")
        String lastName,

        @Length(max = 255, message = "Поле адрес не должен превышать 255 символов")
        @Schema(description = "Адрес продавца")
        String legalAddress,

        @Length(max = 255, message = "Поле имя фирмы не должно превышать 255 символов")
        @Schema(description = "Название фирмы продавца")
        String companyName,

        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$",
                message = "Введённый id фото некорректный. Должен соответствовать" +
                        " ^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$")
        @Schema(description = "Фото продавца")
        String imageId,

        @Schema(description = "Форма регистрации фирмы продавца")
        BusinessModel businessModel,

        @Pattern(regexp = "^[0-9]{10}$",
                message = "Идентификационный номер налогоплательщика должен состоять из 10 цифр")
        @Schema(description = "Идентификационный номер налогоплательщика продавца")
        String itn,

        @Pattern(regexp = "^[0-9]{15}$",
                message = "Основной государственный регистрационный номер должен состоять из 15 цифр")
        @Schema(description = "Основной государственный регистрационный номер продавца")
        String psrn,

        @Pattern(regexp = "^[0-9]{9}$",
                message = "Введённый банковский идентификационный код должен состоять из 9 цифр")
        @Schema(description = "Банковский идентификационный код продавца")
        String bic,

        @Pattern(regexp = "^[0-9]{20}$",
                message = "Введённый банковский расчетный счет должен состоять из 20 цифр")
        @Schema(description = "Банковский расчетный счет продавца")
        String paymentAccount,

        @Pattern(regexp = "^[0-9]{20}$",
                message = "Введённый банковский корпаративный счет должен состоять из 20 цифр")
        @Schema(description = "Банковский корпаративный счет продавца")
        String corporateAccount) {
}