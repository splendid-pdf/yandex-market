package com.yandex.market.userinfoservice.utils;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class PatternConstants {

    public static Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_!#$%&'+=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    public static Pattern NAME_PATTERN = Pattern.compile("[а-яА-Яa-zA-Z]{2,100}");

    public static Pattern PHONE_PATTERN =
            Pattern.compile("((\\+7)|(\\+8)|[78])?((-\\d{3}-)|(\\(\\d{3}\\))|( \\d{3} )|(\\d{3}))\\d{3}[- ]?\\d{2}[- ]?\\d{2}");

    public static Pattern GROUPED_PHONE_NUMBERS_PATTERN = Pattern.compile("(\\d{1})(\\d{3})(\\d{3})(\\d{2})(\\d{2})");

    public static Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,30}$");
}