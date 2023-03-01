package com.yandex.market.productservice.controller;

import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.model.VisibleMethod;
import com.yandex.market.productservice.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.app.seller.url}")
@Tag(name = "API for working with the Product entity for Seller")
public class SellerController {

    private final SellerService sellerService;

    @GetMapping("{sellerId}/products")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductResponseDto> findPageProductsBySellerId(@PathVariable UUID sellerId,
                                                               @PageableDefault(size = 20, sort = "creationDate", direction = Sort.Direction.DESC)
                                                               Pageable pageable) {
        log.info("Received a request to get Page list for products by sellerId = " + sellerId);
        return sellerService.getPageOfProductsBySellerId(sellerId, pageable);
    }

    @PatchMapping("{sellerId}/products")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "deleteOrVisibleProductList",
            summary = "Change product visibility for sellerId",
            description = "Remove or remove / return from sale a list of goods by sellers")
    @ApiResponse(responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class))))
    public void changeProductVisibilityForSeller(@PathVariable(value = "sellerId") UUID sellerId,
                                                 @RequestBody List<UUID> productIds,
                                                 @RequestParam VisibleMethod method,
                                                 @RequestParam boolean methodAction) {
        log.info("A request was received  to change visibility (remove/visibility) for a specific seller with sellerId: {}"
                 + " and a list of goods in the number of {} entries.", sellerId, productIds.size());
        sellerService.changeVisibilityForSellerId(sellerId, productIds, method, methodAction);
    }
}