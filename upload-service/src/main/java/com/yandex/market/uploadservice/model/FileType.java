package com.yandex.market.uploadservice.model;

import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
public enum FileType {
    CHECK("checks/", MediaType.APPLICATION_PDF_VALUE),
    PHOTO("images/avatars/", MediaType.IMAGE_JPEG_VALUE),
    PRODUCT_IMAGE("images/products/", MediaType.IMAGE_JPEG_VALUE);

    private final String folder;

    private final String mediaType;

    FileType(String folder, String mediaType) {
        this.folder = folder;
        this.mediaType = mediaType;
    }
}
