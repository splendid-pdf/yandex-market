package com.marketplace.sellerinfoservice.controller;

import com.marketplace.sellerinfoservice.dto.SellerRegistration;
import com.marketplace.sellerinfoservice.dto.SellerRequestDto;
import com.marketplace.sellerinfoservice.dto.SellerResponseDto;
import com.marketplace.sellerinfoservice.service.SellerService;
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
public class SellerController implements SellerApi {

    private final SellerService sellerService;

    @PostMapping("sellers")
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createSeller(@RequestBody @Valid SellerRegistration sellerRegistration) {
        return sellerService.createSeller(sellerRegistration);
    }

    @GetMapping("sellers/{sellerId}")
    @ResponseStatus(HttpStatus.OK)
    public SellerResponseDto getSellerBySellerId(
            @PathVariable("sellerId") UUID sellerId) {
        return sellerService.getSellerBySellerId(sellerId);
    }

    @PutMapping("sellers/{sellerId}")
    @ResponseStatus(HttpStatus.OK)
    public SellerResponseDto updateSeller(
            @PathVariable UUID sellerId,
            @RequestBody @Valid SellerRequestDto sellerRequestDto) {
        return sellerService.updateSeller(sellerId, sellerRequestDto);
    }

    @DeleteMapping("sellers/{sellerId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSeller(@PathVariable UUID sellerId) {
        sellerService.deleteSellerBySellerId(sellerId);
    }
}