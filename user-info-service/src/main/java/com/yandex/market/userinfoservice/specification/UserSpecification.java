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
    public Specification<User> getSpecificationFromUserFilter(UserFilter userFilter) {
        //todo: change to queue?
        List<Filter> filters = getAllFilters(userFilter);
        Specification<User> specification = where(createSpecification(filters.remove(0)));
        for (Filter input : filters) {
            specification = specification.and(createSpecification(input));
        }
        return specification;
    }

    private List<Filter> getAllFilters(UserFilter userFilter) {

        List<Filter> filters = new ArrayList<>();

        FilterUtilService filterUtilService = new FilterUtilService();

        //todo: OPTIMIZATION COMING...... DONT WORRY...

        val firstName = userFilter.getFirstName();
        if (!StringUtils.isBlank(firstName)) {
            filters.add(filterUtilService.buildFilter(FilterType.EString.FIRST_NAME, firstName));
        }

        val lastName = userFilter.getLastName();
        if (!StringUtils.isBlank(lastName)) {
            filters.add(filterUtilService.buildFilter(FilterType.EString.LAST_NAME, lastName));
        }

        val sex = userFilter.getSex();
        if (sex != null) {
            filters.add(filterUtilService.buildFilter(FilterType.EString.SEX, sex));
        }

        val birthdayFrom = userFilter.getBirthdayFrom();
        if (birthdayFrom != null) {
            filters.add(filterUtilService.buildFilter(FilterType.ELocalDate.BIRTHDAY_FROM, birthdayFrom));
        }

        val birthdayTo = userFilter.getBirthdayTo();
        if (birthdayTo != null) {
            filters.add(filterUtilService.buildFilter(FilterType.ELocalDate.BIRTHDAY_TO, birthdayTo));
        }

        val city = userFilter.getLocation().getCity();
        if(!StringUtils.isBlank(city)){
            filters.add(filterUtilService.buildFilter(FilterType.EString.CITY, city));
        }

        val country = userFilter.getLocation().getCountry();
        if(!StringUtils.isBlank(country)){
            filters.add(filterUtilService.buildFilter(FilterType.EString.COUNTRY, country));
        }

        val street = userFilter.getLocation().getStreet();
        if(!StringUtils.isBlank(street)){
            filters.add(filterUtilService.buildFilter(FilterType.EString.STREET, street));
        }

        val houseNumber= userFilter.getLocation().getHouseNumber();
        if(!StringUtils.isBlank(houseNumber)){
            filters.add(filterUtilService.buildFilter(FilterType.EString.HOUSE_NUMBER, houseNumber));
        }

        //todo: notification filter



        //todo: need to perfom the geopify integration
//        userFilter.getLocation().getLatitude();
//        userFilter.getLocation().getLongitude();
//        userFilter.getLocation().getRadius();

        return filters;

    }

    private Specification<User> createSpecification(Filter filter) {
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

            case LIKE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(filter.getField()),
                                "%" + filter.getAClass().cast(filter.getValue()) + "%");

            default:
                throw new RuntimeException("Operation not supported yet");
        }
    }

}
