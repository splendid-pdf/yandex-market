package com.yandex.market.userinfoservice.specification;

import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.function.BiFunction;

@AllArgsConstructor
public class FilterType {

    private EString eString;
    private ELocalDate eLocalDate;
//    private EBoolean eBoolean;



    public enum EString {
        FIRST_NAME(FilterUtilService::buildFirstNameFilter),
        LAST_NAME(FilterUtilService::buildLastNameFilter),
        SEX(FilterUtilService::buildSexFilter),
        CITY(FilterUtilService::buildCityFilter),
        COUNTRY(FilterUtilService::buildCountryFilter),
        STREET(FilterUtilService::buildStreetFilter),
        HOUSE_NUMBER(FilterUtilService::buildHouseNumberFilter);

        public final BiFunction<FilterUtilService, String, Filter> buildAlgoritmByString;

        EString(BiFunction<FilterUtilService, String, Filter> buildAlgoritmByString) {
            this.buildAlgoritmByString = buildAlgoritmByString;
        }
    }

    public enum ELocalDate {
        BIRTHDAY_FROM(FilterUtilService::buildBirthdayFromFilter),
        BIRTHDAY_TO(FilterUtilService::buildBirthdayToFilter);

        public final BiFunction<FilterUtilService, LocalDate, Filter> buildAlgoritmByDate;

        ELocalDate(BiFunction<FilterUtilService, LocalDate, Filter> buildAlgoritmByDate) {
            this.buildAlgoritmByDate = buildAlgoritmByDate;
        }
    }

//    public enum EBoolean {
//        TO_RECEIVE_ON_ADDRESS(),
//        TO_SEND_PROMOTIONS_AND_MAILING(),
//        TO_SEND_POPULAR_ARTICLES();
//
//        //TODO
//
//        public final BiFunction<FilterUtilService, Boolean, Filter> buildAlgoritmByBoolean;
//
//        EBoolean(BiFunction<FilterUtilService, Boolean, Filter> buildAlgoritmByBoolean) {
//            this.buildAlgoritmByBoolean = buildAlgoritmByBoolean;
//        }
//    }
}