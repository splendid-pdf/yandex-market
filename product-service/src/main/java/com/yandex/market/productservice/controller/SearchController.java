package com.yandex.market.productservice.controller;

import com.yandex.market.productservice.dto.ProductFilterDto;
import com.yandex.market.productservice.dto.ProductResponseDto;
import com.yandex.market.productservice.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("${spring.app.product-controller.url}/search")
@RequiredArgsConstructor
public class SearchController {

    private final ProductSearchService productSearchService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDto> getProductsBySetExternalId(ProductFilterDto productFilterDto,
                                                               @PageableDefault Pageable pageable) {

        return productSearchService.getProductsByFilter(productFilterDto, pageable);
    }

}
