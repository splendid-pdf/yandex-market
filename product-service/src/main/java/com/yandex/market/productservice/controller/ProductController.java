package com.yandex.market.productservice.controller;

import com.yandex.market.productservice.dto.ProductRequestDto;
import com.yandex.market.productservice.dto.response.ProductResponseDto;
import com.yandex.market.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.app.product.url}")
@Tag(name = "API for working with the Product entity")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Invalid data provided to the server",
                content = @Content(mediaType = "application/json"))})
public class ProductController {
    private final ProductService productService;

    @PostMapping("/{sellerExternalId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(operationId = "createProduct", summary = "Create new product for the seller")
    @ApiResponse(responseCode = "201", description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UUID.class)))
    public UUID createProduct(@Parameter(name = "productRequestDto", description = "Representation of a created product")
                              @RequestBody @Valid ProductRequestDto productRequestDto,
                              @Parameter(name = "sellerExternalId", description = "Seller's identifier")
                              @PathVariable("sellerExternalId") UUID sellerExternalId) {
        return productService.createProduct(productRequestDto, sellerExternalId);
    }

    @GetMapping("{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto getProductByExternalId(@PathVariable("externalId") UUID externalId) {
        log.info("Received request to get a product by given value: {}", externalId);
        return productService.getProductByExternalId(externalId);
    }

    @PutMapping("{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto updateProductByExternalId(@PathVariable UUID externalId,
                                                        @RequestBody @Valid ProductRequestDto productRequestDto) {
        log.info("Received request to update a product:{} by given value: {}", externalId, productRequestDto);
        return productService.updateProductByExternalId(externalId, productRequestDto);
    }

    @DeleteMapping("{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProductByExternalId(@PathVariable UUID externalId) {
        log.info("Received request to get a product by given value: {}", externalId);
        productService.deleteProductByExternalId(externalId);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDto> getProductsBySetExternalId(@RequestParam(name = "extId") Set<UUID> externalIdSet,
                                                               @PageableDefault(sort = {"name"}, direction = Sort.Direction.ASC)
                                                               Pageable pageable) {
        log.info("Received request to get a products list by given values: {}", externalIdSet);
        return productService.getProductsBySetExternalId(externalIdSet, pageable);
    }
}