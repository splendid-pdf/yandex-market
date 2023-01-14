package com.yandex.market.userinfoservice.specification;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

public class FilterUtilService {
    Filter buildFirstNameFilter(String firstName) {
        return Filter.builder()
                .field("firstName")
                .operator(QueryOperator.LIKE)
                .value(firstName)
                .build();
    }

    Filter buildLastNameFilter(String lastName) {
        return Filter.builder()
                .field("lastName")
                .operator(QueryOperator.LIKE)
                .value(lastName)
                .build();
    }


    Filter buildBirthdayFromFilter(LocalDate localDate) {
        return Filter.builder()
                .field("birthdayFrom")
                .operator(QueryOperator.GREATER_THAN)
                .value(localDate.toString())
                .build();
    }

    Filter buildBirthdayToFilter(LocalDate localDate) {
        return Filter.builder()
                .field("birthdayTo")
                .operator(QueryOperator.LESS_THAN)
                .value(localDate.toString())
                .build();
    }

    //todo


    public Filter buildFilter(FilterUtils.StringType type, String value) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.f
        restTemplate.getForObject()

        return type.buildAlgoritmByString.apply(this, value);
    }

    public Filter buildFilter(FilterUtils.LocalDateType type, LocalDate value) {
        return type.buildAlgoritmByDate.apply(this, value);
    }

//    public Filter buildFilter(FilterUtils.BooleanType type, Boolean value) {
//        return type.buildAlgoritmByBoolean.apply(this, value);
//    }


}
