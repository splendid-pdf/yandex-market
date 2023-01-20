package com.yandex.market.userinfoservice.utils;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class PatternConstants {

    public Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    public Pattern NAME_PATTERN = Pattern.compile("[а-яА-Яa-zA-Z]{2,100}");

    public Pattern PHONE_PATTERN =
            Pattern.compile("((\\+7)|[78])?((-9\\d{2}-)|(\\(9\\d{2}\\))|( 9\\d{2} )|(9\\d{2}))\\d{3}[- ]?\\d{2}[- ]?\\d{2}");

    public Pattern GROUPED_PHONE_NUMBERS_PATTERN = Pattern.compile("(\\d{1})(\\d{3})(\\d{3})(\\d{2})(\\d{2})");

    public Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,30}$");
}