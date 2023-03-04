package com.yandex.market.sellerinfoservice.controller;

import com.yandex.market.sellerinfoservice.dto.SellerRequestDto;
import com.yandex.market.sellerinfoservice.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${spring.application.url}")
@RequiredArgsConstructor
@Tag(name = "sellers")
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
}