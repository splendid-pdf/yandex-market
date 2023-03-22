package com.yandex.market.userservice.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessagesConstants {
    public static String USER_WITH_THE_SAME_EMAIL_IS_EXISTS_MESSAGE =
            "User with similar email = %s is already exists";
    public static String USER_WITH_THE_SAME_PHONE_IS_EXISTS_MESSAGE =
            "User with similar phone = %s is already exists";

    public static String USER_NOT_FOUND_ERROR_MESSAGE = "User was not found by given externalId = ";
    public static String USER_NOT_FOUND_BY_EMAIL_ERROR_MESSAGE = "User was not found by given email = ";
}