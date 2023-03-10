package com.yandex.market.sellerinfoservice.controller;

import com.yandex.market.sellerinfoservice.controller.response.ErrorResponse;
import com.yandex.market.sellerinfoservice.dto.SellerRequestDto;
import com.yandex.market.sellerinfoservice.dto.SellerResponseDto;
import com.yandex.market.sellerinfoservice.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

@Slf4j
@RestController
@RequestMapping(PUBLIC_API_V1)
@RequiredArgsConstructor
@Tag(name = "Методы seller-service для работы с сущностью \"Продавец\"")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "На сервер переданы неверные данные",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Продавец не найден",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)))})
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("sellers")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создание нового продавца", responses = {
            @ApiResponse(description = "Новый продавец создан", responseCode = "201",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SellerRequestDto.class))),
            @ApiResponse(description = "Ошибка при создании нового продавца", responseCode = "409")
    })
    public UUID createSeller(@RequestBody SellerRequestDto sellerRequestDto) {
        return sellerService.createSeller(sellerRequestDto);
    }

    @GetMapping("sellers/{sellerId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getProductByExternalId", summary = "Получение товара по externalId")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UUID.class)))
    public SellerResponseDto getSellerByExternalId(@PathVariable("sellerId") UUID sellerId) {
        return sellerService.getSellerByExternalId(sellerId);
    }

    @PutMapping("sellers/{sellerId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SellerRequestDto.class))))
    })
    @Operation(operationId = "обновлениеПродавца",
            summary = "Поиск по продавца Id и обновление с помощью Dto",
            description = "Обновление продавца на основе входящего объекта DTO и продавца UUID")
    @ResponseStatus(HttpStatus.OK)
    public SellerResponseDto updateSeller(@PathVariable UUID sellerId, @RequestBody SellerRequestDto sellerRequestDto) {
        return sellerService.updateSellerWithDto(sellerId, sellerRequestDto);
    }

    @DeleteMapping("sellers/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Удаление продавца", responses = {
            @ApiResponse(description = "Продавец успешно удален", responseCode = "200"),
            @ApiResponse(description = "Такого продавца не существует", responseCode = "404")
    })
    public void deleteSeller(@Parameter(name = "externalId", description = "Индентификатор продавца")
                             @PathVariable UUID externalId) {
        sellerService.deleteSellerByExternalId(externalId);
    }
}