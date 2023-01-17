package com.yandex.market.userinfoservice.utils;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class PatternConstants {

    public Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    public Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]{2,100}");

    public Pattern REG_VALID_PHONE = Pattern.compile("([7-8])?(9)(\\d){9}");
}
