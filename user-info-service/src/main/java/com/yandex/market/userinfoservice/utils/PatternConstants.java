package com.yandex.market.userinfoservice.utils;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class PatternConstants {

    public static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    public static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]{2,100}");

    public static final Pattern REG_VALID_PHONE = Pattern.compile("([7-8])?(9)(\\d){9}");
}
