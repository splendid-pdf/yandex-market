package com.yandex.market.exception;

import lombok.Getter;

@Getter
public class SizeLimitFileExceededException extends RuntimeException {
    private Long maxFileSize;
    private Long actualFileSize;

    public SizeLimitFileExceededException(String message, Long maxFileSize, Long actualFileSize) {
        super(message);
        this.maxFileSize = maxFileSize;
        this.actualFileSize = actualFileSize;
    }
}
