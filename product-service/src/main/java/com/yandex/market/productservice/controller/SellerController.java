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
    public void changeProductVisibilityForSeller(@PathVariable(value = "sellerId") UUID sellerId,
                                                 @RequestBody List<UUID> productIds,
                                                 @RequestParam VisibleMethod method,
                                                 @RequestParam boolean methodAction) {
        String methodName = "";
        if (method == VisibleMethod.VISIBLE) {
            if (methodAction) {
                methodName = "withdraw goods from sale";
                sellerService.displayProductListForSeller(productIds, sellerId);
            } else {
                methodName = "return goods for sale";
                log.info("The method of RETURNING the list of products from the archive is executed");
                sellerService.hideProductListForSeller(productIds, sellerId);
            }
        } else if (method == VisibleMethod.DELETE) {
            if (methodAction) {
                methodName = "add products to the archive";
                sellerService.addListOfGoodsToArchiveForSeller(productIds, sellerId);
            } else {
                methodName = "return goods from the archive";
                sellerService.returnListOfGoodsFromArchiveToSeller(productIds, sellerId);
            }
        }
        log.info("A request was received to {} for a specific seller with sellerId: {}"
                 + " and a list of goods in the number of {} entries.", methodName, sellerId, productIds.size());
    }


    @DeleteMapping("{sellerId}/products")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "removeProductsFromArchive",
            summary = "Removing a list of products from the database",
            description = "If the product is in the archive (isDeleted = true), then it can be deleted from the database. " +
                          "The list of products from the archive is accepted as input")
    @ApiResponse(responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class))))
    public void deleteListProductBySellerId(@PathVariable(value = "sellerId") UUID sellerId,
                                            @RequestBody List<UUID> productIds) {
        log.info("Request for the complete removal of the product(s) in the amount of {} pieces " +
                 "for the seller with externalId = {}", productIds.size(), sellerId);
        sellerService.deleteFromArchiveListProductBySellerId(productIds, sellerId);
    }
}
