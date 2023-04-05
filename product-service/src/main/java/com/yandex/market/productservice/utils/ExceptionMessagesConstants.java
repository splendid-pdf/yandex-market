package com.yandex.market.productservice.utils;

public final class ExceptionMessagesConstants {
    public static final String PRODUCT_NOT_FOUND_ERROR_MESSAGE = "Product was not found by given externalId = %s";
    public static final String ROOM_NOT_FOUND_ERROR_MESSAGE = "Room was not found by given externalId = %s";
    public static final String TYPE_NOT_FOUND_ERROR_MESSAGE = "Type was not found by given externalId = %s";
    public static final String PRODUCT_CHARACTERISTIC_NOT_FOUND_ERROR_MESSAGE = "Product characteristic was not found " +
            "by given externalId = %s";
    public static final String SPECIAL_PRICE_NOT_FOUND_ERROR_MESSAGE = "Special price was not found " +
            "by given externalId = %s";

    public static final String INVALID_CHARACTERISTICS_SIZE = "Incorrect number of characteristics. " +
            "Required = %s. Requested = %s";

    public static final String INVALID_CHARACTERISTIC_TYPE = "Invalid characteristic type name = %s " +
            "Required: valueType = %s. Requested: valueType = %s";

    public static final String INVALID_CHARACTERISTIC = "Invalid characteristic. name = %s valueType = %s ";

    public static final String INVALID_CHARACTERISTIC_VALUE = "Invalid characteristic. name = %s value = %s. " +
            "Can not parse to valueType = %s";

    private ExceptionMessagesConstants() {
    }
}
