package com.yandex.market.shopservice.controllers;

import com.yandex.market.shopservice.dto.ShopSystemDto;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.service.ShopSystemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.url}")
public class ShopSystemController {
    private final ShopSystemService shopService;
    @Value("${spring.application.url}")
    private String REQUEST_NAME;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<ShopSystemDto> getAllShopSystems(@PageableDefault(size = 20) Pageable pageable) {
        log.info("GET " + REQUEST_NAME +
                ". Received a request to get paginated list of shop systems. page = " + pageable +
                ", size = " + pageable.getPageSize());
        return shopService.getAllShopSystems(pageable);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewShopSystem(@RequestBody @Valid ShopSystemDto shopSystemDto) {
        log.info("POST " + REQUEST_NAME +
                ". Received a request to create new shop system: %s".formatted(shopSystemDto));
        shopService.createShopSystem(shopSystemDto);
    }

    @GetMapping("/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public ShopSystem getShopSystemByExternalId(@PathVariable("externalId") UUID externalId) {
        log.info("GET" + REQUEST_NAME + "/" + externalId +
                ". Received a request to get shop system by external id = %s".formatted(externalId));
        return shopService.getShopSystemByExternalId(externalId);
    }

    @DeleteMapping("/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSystemShopByExternalId(@PathVariable("externalId") UUID externalId) {
        log.info("GET " + REQUEST_NAME + "/" + externalId +
                ". Received a request to delete a shop system by external id = %s".formatted(externalId));
        shopService.deleteSystemShopByExternalId(externalId);
    }
}
