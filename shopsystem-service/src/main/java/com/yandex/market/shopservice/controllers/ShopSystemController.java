package com.yandex.market.shopservice.controllers;

import com.yandex.market.shopservice.dto.ShopSystemDto;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.service.ShopSystemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/public/api/v1/shopsystems")
public class ShopSystemController {
    private final ShopSystemService shopService;
    private final String REQUEST_NAME = "/public/api/v1/shopsystems";

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<ShopSystemDto> getAllShopSystems(@PageableDefault(size = 20) Pageable pageable) {
        log.info("GET " + REQUEST_NAME +
                ". Get request received. Pageable parameters: " + pageable);
        return shopService.getAllShopSystems(pageable);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewShopSystem(@RequestBody @Valid ShopSystemDto shopSystemDto) {
        log.info("POST " + REQUEST_NAME +
                ". A request was received to add an organization. ShopSystemDto parameters: " + shopSystemDto);
        shopService.createShopSystem(shopSystemDto);
    }

    @GetMapping("/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public ShopSystem getShopSystemByExternalId(@PathVariable("externalId") UUID externalId) {
        log.info("GET" + REQUEST_NAME + "/" + externalId +
                ". A request was received to search for an organization. Parameters ExternalId: " + externalId);
        return shopService.getShopSystemByExternalId(externalId);
    }

    @DeleteMapping("/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSystemShopByExternalId(@PathVariable("externalId") UUID externalId) {
        log.info("GET " + REQUEST_NAME + "/" + externalId +
                ". Request received to delete organization. ExternalId parameters: " + externalId);
        shopService.deleteSystemShopByExternalId(externalId);
    }
}
