package com.yandex.market.uploadservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DownloadFileInfo {
    private byte[] content;
    private String filename;
    private String contentType;
}