package com.yandex.market.shopservice.controllers;

import com.yandex.market.shopservice.dto.requests.ShopSystemRequestDto;
import com.yandex.market.shopservice.dto.responses.ShopSystemResponsesDto;
import com.yandex.market.shopservice.service.ShopSystemServiceImpl;
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
@RequestMapping("${spring.application.url}")
public class ShopSystemController {
    private final ShopSystemServiceImpl shopService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<ShopSystemResponsesDto> getAllShopSystems(@PageableDefault(size = 20) Pageable pageable) {
        log.info("Received a request to get paginated list of shop systems. page = " + pageable +
                ", size = " + pageable.getPageSize());
        return shopService.getAllShopSystems(pageable);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewShopSystem(@RequestBody @Valid ShopSystemRequestDto shopSystemDto) {
        log.info("Received a request to create new shop system: %s".formatted(shopSystemDto));
        shopService.createShopSystem(shopSystemDto);
    }

    @GetMapping("/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public ShopSystemResponsesDto getShopSystemByExternalId(@PathVariable("externalId") UUID externalId) {
        log.info("Received a request to get shop system by external id = %s".formatted(externalId));
        return shopService.getShopSystemByExternalId(externalId);
    }

    @DeleteMapping("/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSystemShopByExternalId(@PathVariable("externalId") UUID externalId) {
        log.info("Received a request to delete a shop system by external id = %s".formatted(externalId));
        shopService.deleteSystemShopByExternalId(externalId);
    }

    @PutMapping("/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateSystemShopByExternalId(@PathVariable("externalId") UUID externalId,
                                             @RequestBody @Valid ShopSystemRequestDto shopSystemDtoRequest) {
        log.info("Received a request to update a shop system by external id = %s".formatted(externalId));
        shopService.updateSystemShopByExternalId(externalId, shopSystemDtoRequest);
    }
}
