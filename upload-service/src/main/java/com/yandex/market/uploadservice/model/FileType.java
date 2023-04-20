package com.yandex.market.uploadservice.model;

import lombok.Getter;

@Getter
public enum FileType {
    //todo: подумать нужен ли будет этот класс и удалить его в дальнейшем если не нужен
    CHECK("checks/"),
    PHOTO("images/avatars/"),
    PRODUCT_IMAGE("images/products/");

    private final String folder;

    FileType(String folder) {
        this.folder = folder;
    }
}