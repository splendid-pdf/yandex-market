package com.yandex.market.util;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class FileUtils {

    public static URI getFileUrl(@NotNull String path, ClassLoader classLoader) {
        try {
            return Objects.requireNonNull(classLoader.getResource(path)).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException("File not found");
        }
    }
}
