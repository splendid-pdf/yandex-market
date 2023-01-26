package com.yandex.market.orderservice.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessagesConstants {

    public String ORDER_NOT_FOUND_ERROR_MESSAGE = "Order was not found by given externalId = ";
    public String ORDER_COMPLETED_ERROR_MESSAGE = "Order has been completed and cannot be changed. externalId = ";
}