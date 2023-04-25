package com.yandex.market.uploadservice.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String MAX_FILE_SIZE_EXCEPTION_MESSAGE =
            "%s file too large";
    public static final String EMPTY_FILE_EXCEPTION_MESSAGE =
            "put some object";
    public static final String NOT_PERMITTED_FILE_EXTENSION_EXCEPTION_MESSAGE =
            "%s the uploaded file has an unsupported extension";
    public static final String NOT_PERMITTED_FILE_TYPE_BY_FILE_EXTENSION_EXCEPTION_MESSAGE =
            "%s the file type = %s is not supported by the file extension = %s";
    public static final String UPLOAD_FILE_EXCEPTION_MESSAGE =
            "%s unable to upload a file";

    public static final String ENTITIES_WAS_NOT_FOUND_BY_IDS_LIST_EXCEPTION_MESSAGE =
            "Entities was not found in list";

}