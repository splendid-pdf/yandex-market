package com.yandex.market.sellerinfoservice.controller;

import com.yandex.market.sellerinfoservice.dto.SellerRegistration;
import com.yandex.market.sellerinfoservice.dto.SellerRequestDto;
import com.yandex.market.sellerinfoservice.dto.SellerResponseDto;
import com.yandex.market.sellerinfoservice.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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
public class SellerController implements SellerApi{

    private final SellerService sellerService;

    @PostMapping("sellers")
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createSeller(@RequestBody @Valid SellerRegistration sellerRegistration) {
        return sellerService.createSeller(sellerRegistration);
    }

    @GetMapping("sellers/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public SellerResponseDto getSellerByExternalId(
            @Parameter(name = "externalId", description = "Идентификатор продавца")
            @PathVariable("externalId") UUID externalId) {
        return sellerService.getSellerByExternalId(externalId);
    }

    @PutMapping("sellers/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public SellerResponseDto updateSeller(
            @Parameter(name = "externalId", description = "Идентификатор продавца")
            @PathVariable UUID externalId,
            @RequestBody SellerRequestDto sellerRequestDto) {
        return sellerService.updateSellerWithDto(externalId, sellerRequestDto);
    }

    @DeleteMapping("sellers/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSeller(@Parameter(name = "externalId", description = "Идентификатор продавца")
                             @PathVariable UUID externalId) {
        sellerService.deleteSellerByExternalId(externalId);
    }
}