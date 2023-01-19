package com.yandex.market.userinfoservice.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessagesConstants {
    public String USER_WITH_THE_SAME_EMAIL_IS_EXISTS_MESSAGE =
            "User with similar email = %s is already exists";
    public String USER_WITH_THE_SAME_PHONE_IS_EXISTS_MESSAGE =
            "User with similar phone = %s is already exists";

    public String USER_NOT_FOUND_ERROR_MESSAGE = "User was not found by given externalId = ";
    public String USER_NOT_FOUND_MESSAGE_BY_VALUE = "User was not found by given value = ";
}