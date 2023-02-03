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
        List<Specification> specifications = new ArrayList<>();
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

        if(productFilterDto.maxWeight() != null) {
            specifications.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(root.get("weight"), productFilterDto.minWeight()));
        }

        if(specifications.isEmpty()) {
            return null;
        }


        }
    }
}
