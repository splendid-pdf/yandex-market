package com.yandex.market.uploadservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileSizeRestriction {
    //todo: мб этот класс удалить можно
    private Long maxFileSizeInBytes;
}