package com.yandex.market.userinfoservice.specification;

import com.yandex.market.userinfoservice.model.User;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.openapitools.api.model.UserFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Component
@RequiredArgsConstructor
public class UserSpecification {
    private final FilterOrganizer filterOrganizer;

    public Specification<User> getSpecificationFromUserFilter(@NotNull UserFilter userFilter) {
        //todo: change to queue?
        List<Filter> filters = getAllFilters(userFilter);
        if (filters.isEmpty()) return null;
        Specification<User> specification = where(createSpecification(filters.remove(0)));
        for (Filter input : filters) {
            specification = specification.and(createSpecification(input));
        }
        return specification;
    }

    private List<Filter> getAllFilters(@NotNull UserFilter userFilter) {

        List<Filter> filters = new ArrayList<>();

        Arrays.stream(UserFieldFiltration.values())
                .forEach(field -> field.buildFilter.accept(filterOrganizer, userFilter, filters));

        //todo: filtering by longitude & latitude

        return filters;
    }

    private Specification<User> createSpecification(Filter filter) {

        //TODO: OPTIMIZATION COMING FOR SWITCH CASES...

        switch (filter.getOperator()) {
            case EQUALS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(filter.getField()),
                                filter.getAClass().cast(filter.getValue()));

            case NOT_EQ:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(filter.getField()),
                                filter.getAClass().cast(filter.getValue()));

            case GREATER_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThan(root.get(filter.getField()),
                                filter.getAClass().cast(filter.getValue()));

            case LESS_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThan(root.get(filter.getField()),
                                filter.getAClass().cast(filter.getValue()));

            case LOCATION_LIKE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get("location").get(filter.getField()),
                                "%" + filter.getAClass().cast(filter.getValue()) + "%");

            case LOCATION_EQUALS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("location").get(filter.getField()),
                                filter.getAClass().cast(filter.getValue()));

            case NOTIFICATION_EQUALS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("notificationSettings").get(filter.getField()),
                                filter.getAClass().cast(filter.getValue()));

            case LIKE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(filter.getField()),
                                "%" + filter.getAClass().cast(filter.getValue()) + "%");

            default:
                throw new RuntimeException("Operation not supported yet");
        }
    }
}