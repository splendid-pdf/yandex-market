package com.yandex.market.productservice.controller;

import com.yandex.market.productservice.dto.ProductRepresentationSetDto;
import com.yandex.market.productservice.dto.projections.ProductPreview;
import com.yandex.market.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

@RestController
@RequestMapping(PUBLIC_API_V1)
@RequiredArgsConstructor
@Tag(name = "Products Previews")
public class ProductPreviewController {

    private final ProductService productService;

    @GetMapping("/product-previews")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "getProductsPreviews",
            description = "Получение превью всех товаров с сортировкой по убыванию времени создания")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductPreview.class)))
    public Page<ProductPreview> getAllProductPreviews(
            @PageableDefault Pageable pageable) {
        return productService.getProductPreviews(pageable);
    }

    @PostMapping("/product-previews")
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "getNewestProductPreviewsByIdentifiers", description = "Получение превью самых новых товаров")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductPreview.class)))
    public List<ProductPreview> getProductPreviewsByIdentifiers(@RequestBody ProductRepresentationSetDto productRepresentationSetDto) {
        return productService.getProductPreviewsByIds(productRepresentationSetDto.productIdentifiers());
    }
}