package com.yandex.market.uploadservice.validator;

import com.yandex.market.exception.SizeLimitFileExceededException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component
public class FileValidator {

    private static final long MAX_JPG_FILE_SIZE = 1024 * 200L;
    private static final long MAX_PDF_FILE_SIZE = 1024 * 50L;

    public void validate(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("Put some object");
        } else if (
                Objects.equals(getExtension(multipartFile.getOriginalFilename()), "pdf")
                        && multipartFile.getSize() > MAX_PDF_FILE_SIZE
        ) {
            throw new SizeLimitFileExceededException(
                    "File too large",
                    multipartFile.getSize(),
                    MAX_PDF_FILE_SIZE
            );
        } else if (
                Objects.equals(getExtension(multipartFile.getOriginalFilename()), "jpg")
                        && multipartFile.getSize() > MAX_JPG_FILE_SIZE
        ) {
            throw new SizeLimitFileExceededException(
                    "File too large",
                    multipartFile.getSize(),
                    MAX_JPG_FILE_SIZE
            );
        }
    }

    private String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }
}
