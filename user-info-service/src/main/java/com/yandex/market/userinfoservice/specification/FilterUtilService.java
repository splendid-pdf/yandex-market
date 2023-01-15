package com.yandex.market.userinfoservice.specification;

import com.yandex.market.userinfoservice.model.Sex;

import java.time.LocalDate;

public class FilterUtilService {
    Filter buildFirstNameFilter(String firstName) {
        return Filter.builder()
                .aClass(String.class)
                .field("firstName")
                .operator(QueryOperator.LIKE)
                .value(firstName)
                .build();
    }

    Filter buildLastNameFilter(String lastName) {
        return Filter.builder()
                .aClass(String.class)
                .field("lastName")
                .operator(QueryOperator.LIKE)
                .value(lastName)
                .build();
    }

    Filter buildSexFilter(String sex) {
        return Filter.builder()
                .aClass(Sex.class)
                .field("sex")
                .operator(QueryOperator.EQUALS)
                .value(Sex.valueOf(sex))
                .build();
    }

    Filter buildBirthdayFromFilter(LocalDate localDate) {
        return Filter.builder()
                .aClass(LocalDate.class)
                .field("birthday")
                .operator(QueryOperator.GREATER_THAN)
                .value(localDate)
                .build();
    }

    Filter buildBirthdayToFilter(LocalDate localDate) {
        return Filter.builder()
                .aClass(LocalDate.class)
                .field("birthday")
                .operator(QueryOperator.LESS_THAN)
                .value(localDate)
                .build();
    }

    Filter buildCityFilter(String city) {
        return Filter.builder()
                .aClass(String.class)
                .field("city")
                .operator(QueryOperator.LIKE)
                .value(city)
                .build();
    }


    Filter buildCountryFilter(String country) {
        return Filter.builder()
                .aClass(String.class)
                .field("country")
                .operator(QueryOperator.LIKE)
                .value(country)
                .build();
    }

    Filter buildStreetFilter(String street) {
        return Filter.builder()
                .aClass(String.class)
                .field("street")
                .operator(QueryOperator.LIKE)
                .value(street)
                .build();
    }

    Filter buildHouseNumberFilter(String houseNumber) {
        return Filter.builder()
                .aClass(String.class)
                .field("houseNumber")
                .operator(QueryOperator.EQUALS)
                .value(houseNumber)
                .build();
    }

    //todo


    public Filter buildFilter(FilterType.EString type, String value) {
        return type.buildAlgoritmByString.apply(this, value);
    }

    public Filter buildFilter(FilterType.ELocalDate type, LocalDate value) {
        return type.buildAlgoritmByDate.apply(this, value);
    }

    //for notification
//    public Filter buildFilter(FilterUtils.BooleanType type, Boolean value) {
//        return type.buildAlgoritmByBoolean.apply(this, value);
//    }


}
