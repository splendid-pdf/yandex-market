package com.yandex.market.uploadservice.model;

import lombok.*;

import java.net.URL;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileAttributes {
    private String filename;
    private URL url;
}
