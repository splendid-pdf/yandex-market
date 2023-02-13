package com.yandex.market.uploadservice.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static String [] SUPPORTED_CONTENT_TYPES = {"image/jpeg", "image/jpg", "image/png", "application/pdf"};
    public static String MAX_FILE_SIZE_EXCEPTION_MESSAGE = "File too large";
    public static String EMPTY_FILE_EXCEPTION_MESSAGE = "Put some object";
    public static String PERMITTED_FILE_TYPES_EXCEPTION_MESSAGE = "Only PDF, PNG or JPG images are allowed";
    public static String NUMBER_OF_FILES_UPLOADED_EXCEEDED_EXCEPTION_MESSAGE =
            "Maximum number of files to work with must be less or equal to ";
    public static String UPLOAD_FILE_EXCEPTION_MESSAGE = "Unable to upload a file = %s";
    public static String GET_FILE_URL_EXCEPTION_MESSAGE = "Unable to find a file by fileId = %s";
    public static String DOWNLOAD_FILE_EXCEPTION_MESSAGE = "Unable to find a file by key = %s";
}