package com.yandex.market.productservice.repository;

import com.yandex.market.productservice.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    void deleteByUrl(String url);

    Optional<ProductImage> findByUrl(String url);

}
