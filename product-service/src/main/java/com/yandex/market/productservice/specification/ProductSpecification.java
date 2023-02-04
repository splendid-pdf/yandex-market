package com.yandex.market.productservice.specification;

import com.yandex.market.productservice.dto.ProductFilterDto;
import com.yandex.market.productservice.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductSpecification {

    public Specification<Product> getSpecificationFromUserFilter(ProductFilterDto productFilterDto) {
        List<Specification<Product>> specifications = new ArrayList<>();

        if(productFilterDto == null)
            return null;

        if(productFilterDto.name() != null) {
            specifications.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + productFilterDto.name() + "%"));
            }

        if(productFilterDto.maxWeight() != null) {
            specifications.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("weight"), productFilterDto.maxWeight()));
        }

        if(productFilterDto.minWeight() != null) {
            specifications.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(root.get("weight"), productFilterDto.minWeight()));
        }

        if(productFilterDto.minRating() != null) {
            specifications.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(root.get("rating"), productFilterDto.minRating()));
        }

        if(productFilterDto.manufacturers() != null) {
            productFilterDto.manufacturers()
                    .forEach(manufacturer ->
                            specifications.add((root, query, criteriaBuilder) ->
                                    criteriaBuilder.equal(root.get("manufacturer"), manufacturer)));
        }

        if(productFilterDto.productTypes() != null) {
            productFilterDto.productTypes()
                    .forEach(productType ->
                            specifications.add((root, query, criteriaBuilder) ->
                                    criteriaBuilder.equal(root.get("productType"), productType)));
        }

        if(specifications.isEmpty())
            return null;

       return specifications
               .stream()
               .reduce(Specification::and)
               .orElse(null);
    }
}
