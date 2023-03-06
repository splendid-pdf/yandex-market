package com.yandex.market.sellerinfoservice.controller;

import com.yandex.market.sellerinfoservice.dto.SellerRequestDto;
import com.yandex.market.sellerinfoservice.dto.SellerResponseDto;
import com.yandex.market.sellerinfoservice.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
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

@Slf4j
@RestController
@RequestMapping("${spring.application.url}")
@RequiredArgsConstructor
@Tag(name = "Методы seller-service для работы с сущьностью \"Продавец\"")
public class SellerController {

    private final SellerService sellerService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Создание нового продавца", responses = {
            @ApiResponse(description = "Новый продавец создан", responseCode = "201",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SellerRequestDto.class))),
            @ApiResponse(description = "Ошибка при создании нового продавца", responseCode = "409")
    })
    public UUID createSeller(@RequestBody SellerRequestDto sellerRequestDto) {
        return sellerService.createSeller(sellerRequestDto);
    }

    @PutMapping("{sellerId}")
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
}