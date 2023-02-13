package com.yandex.market.uploadservice.model;

import lombok.Getter;

@Getter
public enum FileType {
    CHECK("checks/"),
    PHOTO("images/avatars/"),
    PRODUCT_IMAGE("images/products/");

    private final String folder;

    FileType(String folder) {
        this.folder = folder;
    }
}