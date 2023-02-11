package com.yandex.market.productservice.service;

import com.yandex.market.productservice.dto.request.ProductPriceRequestDto;
import com.yandex.market.productservice.dto.response.ProductFullInfoResponse;
import com.yandex.market.productservice.dto.response.ProductPriceResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Service
public interface ProductsByShopService {

    UUID createProductPrice(ProductPriceRequestDto dto);
    Page<ProductFullInfoResponse> getProductsByShop(@PathVariable UUID shopId, Pageable pageable);

    Page<ProductFullInfoResponse> getProductsByBranch(UUID branchId, Pageable pageable);

    ProductPriceResponseDto getProductsByExternalId(UUID externalId);
}
