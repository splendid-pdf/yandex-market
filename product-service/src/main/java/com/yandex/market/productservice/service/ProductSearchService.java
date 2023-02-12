package com.yandex.market.productservice.service;

import com.yandex.market.productservice.dto.ProductFilterDto;
import com.yandex.market.productservice.dto.ProductResponseDto;
import com.yandex.market.productservice.mapper.ProductMapper;
import com.yandex.market.productservice.repository.ProductRepository;
import com.yandex.market.productservice.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductSpecification productSpecification;

    public List<ProductResponseDto> getProductsByFilter(ProductFilterDto productFilterDto, Pageable pageable) {

        return productRepository.findAll(productSpecification.getSpecificationByFilter(productFilterDto), pageable)
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }
}
