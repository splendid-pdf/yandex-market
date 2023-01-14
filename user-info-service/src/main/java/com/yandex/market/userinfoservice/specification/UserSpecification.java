package com.yandex.market.userinfoservice.specification;

import com.yandex.market.userinfoservice.model.User;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.api.model.UserFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;
public class UserSpecification {
    public static Specification<User> getSpecificationFromUserFilter(UserFilter userFilter) {
        //todo: change to queue?
        List<Filter> filters = getAllFilters(userFilter);
        Specification<User> specification = where(createSpecification(filters.remove(0)));
        for (Filter input : filters) {
            specification = specification.and(createSpecification(input));
        }
        return specification;
    }

    private static List<Filter> getAllFilters(UserFilter userFilter) {

        List<Filter> filters = new ArrayList<>();

        FilterUtilService filterUtilService = new FilterUtilService();

        val firstName = userFilter.getFirstName();
        if (!StringUtils.isBlank(firstName)) {
            filters.add(filterUtilService.buildFilter(FilterUtils.StringType.FIRST_NAME, firstName));
        }

        val lastName = userFilter.getLastName();
        if (!StringUtils.isBlank(lastName)) {
            filters.add(filterUtilService.buildFilter(FilterUtils.StringType.LAST_NAME, lastName));
        }

        //todo:


        return filters;

    }

    private static Specification<User> createSpecification(Filter input) {
        switch (input.getOperator()) {
            case EQUALS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(input.getField()),
                                input.getValue());
            case NOT_EQ:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(input.getField()),
                                input.getValue());
            case GREATER_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThan(root.get(input.getField()),
                                input.getValue());
            case LESS_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThan(root.get(input.getField()),
                                input.getValue());
            case LIKE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(input.getField()), "%" + input.getValue() + "%");

            default:
                throw new RuntimeException("Operation not supported yet");
        }
    }

}
