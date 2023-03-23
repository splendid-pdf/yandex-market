package com.yandex.market.productservice.model;

import java.time.LocalDate;

public enum ValueType {

    TEXT {
        public String parse(String value) {
            return null;
        }
    }, LONG(){
        public Long parse(String value) {
            return Long.parseLong(value);
        }
    }, DOUBLE {
        public Double parse(String value) {
            return Double.parseDouble(value);
        }
    }, BOOLEAN {
        public Boolean parse(String value) {
            return Boolean.parseBoolean(value);
        }
    }, LOCAL_DATE {
        public LocalDate parse(String value) {
            return LocalDate.parse(value);
        }
    }

}