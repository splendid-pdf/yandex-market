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

    public Specification<Product> getSpecificationByFilter(ProductFilterDto productFilterDto) {
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
            specifications.add(productFilterDto.manufacturers()
                    .stream()
                    .map(manufacturer ->
                            (Specification<Product>) (root, query, criteriaBuilder)
                            -> criteriaBuilder.equal(root.get("manufacturer"), manufacturer))
                    .reduce(Specification::or)
                    .orElseThrow());
        }

        if(productFilterDto.productTypes() != null) {
            specifications.add(productFilterDto.productTypes()
                    .stream()
                    .map(productType ->
                            (Specification<Product>) (root, query, criteriaBuilder)
                                    -> criteriaBuilder.equal(root.get("productType"), productType))
                    .reduce(Specification::or)
                    .orElseThrow());
        }

        if (productFilterDto.minLength() != null) {
            specifications.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(root.get("length"), productFilterDto.minLength()));
            specifications.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("length"), "%" + productFilterDto.minLength().split(" ")[1]));
        }

        if (productFilterDto.maxLength() != null) {
            specifications.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("length"), productFilterDto.maxLength()));
            specifications.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("length"), "%" + productFilterDto.maxLength().split(" ")[1]));
        }

        if (productFilterDto.minWidth() != null) {
            specifications.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(root.get("width"), productFilterDto.minWidth()));
            specifications.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("width"), "%" + productFilterDto.minWidth().split(" ")[1]));
        }

        if (productFilterDto.maxWidth() != null) {
            specifications.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("width"), productFilterDto.maxWidth()));
            specifications.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("width"), "%" + productFilterDto.maxWidth().split(" ")[1]));
        }

        if(specifications.isEmpty())
            return null;

       return specifications
               .stream()
               .reduce(Specification::and)
               .orElse(null);
    }
}
