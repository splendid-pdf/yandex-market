package com.marketplace.sellerinfoservice.controller;

import com.marketplace.sellerinfoservice.dto.SellerRegistration;
import com.marketplace.sellerinfoservice.dto.SellerRequestDto;
import com.marketplace.sellerinfoservice.dto.SellerResponseDto;
import com.yandex.market.model.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@Tag(name = "Seller")
@ApiResponses({
        @ApiResponse(
                responseCode = "400",
                description = "На сервер переданы неверные данные",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )),
        @ApiResponse(
                responseCode = "401",
                description = "Продавец не авторизован",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )),
        @ApiResponse(
                responseCode = "403",
                description = "У продавца нет доступа к ресурсу",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )),
        @ApiResponse(
                responseCode = "404",
                description = "Ресурс не был найден по id",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                ))
})
public interface SellerApi {

    @Operation(summary = "Создание нового продавца",
            responses = {@ApiResponse(responseCode = "201",
                    description = "Новый продавец создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UUID.class))),
                    @ApiResponse(responseCode = "409", description = "Ошибка при создании нового продавца")
            })
    UUID createSeller(@RequestBody @Valid SellerRegistration sellerRegistration);

    @Operation(operationId = "getSellerByExternalId", summary = "Получение продавца по externalId")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Продавец успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SellerResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Такого продавца не существует")
    })
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #externalId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )
            """
    )
    SellerResponseDto getSellerByExternalId(
            @Parameter(name = "externalId", description = "Идентификатор продавца")
            @PathVariable("externalId") UUID externalId);

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Продавец успешно обновлён",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SellerResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Такого продавца не существует")
    })
    @Operation(operationId = "обновлениеПродавца",
            summary = "Поиск по продавца Id и обновление с помощью Dto",
            description = "Обновление продавца на основе входящего объекта DTO и продавца UUID")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #externalId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )
            """
    )
    SellerResponseDto updateSeller(
            @Parameter(name = "externalId", description = "Индификатор продавца")
            @PathVariable UUID externalId,
            @RequestBody SellerRequestDto sellerRequestDto);

    @Operation(summary = "Удаление продавца", responses = {
            @ApiResponse(responseCode = "200", description = "Продавец успешно удален"),
            @ApiResponse(responseCode = "404", description = "Такого продавца не существует")
    })
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #externalId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )
            """
    )
    void deleteSeller(@Parameter(name = "externalId", description = "Идентификатор продавца")
                      @PathVariable UUID externalId);
}
