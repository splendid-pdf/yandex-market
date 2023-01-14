package com.yandex.market.userinfoservice.specification;

import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.function.BiFunction;

@AllArgsConstructor
public class FilterUtils {

    private StringType stringType;
    private LocalDateType localDateType;
//    private BooleanType booleanType;



    public enum StringType {
        FIRST_NAME(FilterUtilService::buildFirstNameFilter),
        LAST_NAME(FilterUtilService::buildLastNameFilter);
        //TODO
        public final BiFunction<FilterUtilService, String, Filter> buildAlgoritmByString;

        StringType(BiFunction<FilterUtilService, String, Filter> buildAlgoritmByString) {
            this.buildAlgoritmByString = buildAlgoritmByString;
        }
    }

    public enum LocalDateType {
        BIRTHDAY_FROM(FilterUtilService::buildBirthdayFromFilter),
        BIRTHDAY_TO(FilterUtilService::buildBirthdayToFilter);


        public final BiFunction<FilterUtilService, LocalDate, Filter> buildAlgoritmByDate;

        LocalDateType(BiFunction<FilterUtilService, LocalDate, Filter> buildAlgoritmByDate) {
            this.buildAlgoritmByDate = buildAlgoritmByDate;
        }
    }

//    public enum BooleanType {
//        TO_RECEIVE_ON_ADDRESS(),
//        TO_SEND_PROMOTIONS_AND_MAILING(),
//        TO_SEND_POPULAR_ARTICLES();
//
//        //TODO
//
//        public final BiFunction<FilterUtilService, Boolean, Filter> buildAlgoritmByBoolean;
//
//        BooleanType(BiFunction<FilterUtilService, Boolean, Filter> buildAlgoritmByBoolean) {
//            this.buildAlgoritmByBoolean = buildAlgoritmByBoolean;
//        }
//    }
}