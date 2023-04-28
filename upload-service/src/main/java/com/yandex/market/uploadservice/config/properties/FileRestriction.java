package com.yandex.market.uploadservice.config.properties;

import com.yandex.market.uploadservice.model.FileType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FileRestriction {
    private Long maxFileSizeInBytes;
    private List<FileType> fileTypes;
}
