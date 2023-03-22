package com.yandex.market.productservice.repository;

import com.yandex.market.productservice.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    void deleteByUrl(String url);

}
