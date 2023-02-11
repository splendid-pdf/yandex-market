package com.yandex.market.uploadservice;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@TestPropertySource(locations = "classpath:application-test.yaml")
@ActiveProfiles("test")
@SpringBootTest(classes = UploadServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class UploadServiceApplicationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AmazonS3 aws;

    @Test
    void uploadStatus200() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", Files.readAllBytes(
                Paths.get("src", "test", "resources", "photo.jpg"))
        );

        Mockito.when(aws.putObject(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(InputStream.class),
                ArgumentMatchers.any(ObjectMetadata.class))
        ).thenReturn(new PutObjectResult());

        mvc.perform(MockMvcRequestBuilders.multipart("/public/api/v1/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .param("fileId", "id0000")
                        .param("fileType", "CHECK"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(aws, Mockito.times(1)).putObject(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(InputStream.class),
                ArgumentMatchers.any(ObjectMetadata.class));
    }

    @Test
    void getFileUrlByIdStatus200() throws Exception {

//        Mockito.when(aws.generatePresignedUrl(
//                ArgumentMatchers.anyString(),
//                ArgumentMatchers.anyString(),
//                ArgumentMatchers.any(Date.class)
//        )).thenReturn(new URL("https://stackoverflow.com/questions/7692298/inputstream-from-relative-path"));
//
//        mvc.perform(MockMvcRequestBuilders.get("/public/api/v1/getFileUrlById")
//                        .param("fileId", "id0000")
//                        .param("fileType", "CHECK"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("\"https://stackoverflow.com/questions/7692298/inputstream-from-relative-path\""));
//
//        Mockito.verify(aws, Mockito.times(1)).generatePresignedUrl(
//                ArgumentMatchers.anyString(),
//                ArgumentMatchers.anyString(),
//                ArgumentMatchers.any(Date.class)
//        );
    }

    @Test
    void downloadStatus200() throws Exception {
        S3Object s3Object = Mockito.mock(S3Object.class);
        S3ObjectInputStream s3ObjectInputStream = Mockito.mock(S3ObjectInputStream.class);
        Mockito.when(aws.getObject(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                ))
                .thenReturn(s3Object);

        Mockito.when(s3Object.getObjectContent()).thenReturn(s3ObjectInputStream);

        Mockito.when(s3ObjectInputStream.readAllBytes()).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.get("/public/api/v1/download")
                .param("fileId", "id0000")
                .param("fileType", "CHECK"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists(Headers.CONTENT_DISPOSITION))
                .andExpect(MockMvcResultMatchers.header().exists(Headers.CONTENT_TYPE))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PDF_VALUE));

        Mockito.verify(aws, Mockito.times(1)).getObject(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString());
    }

    @Test
    void deleteStatus200() throws Exception {
        Mockito.doNothing().when(aws).deleteObject(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
        );

        mvc.perform(MockMvcRequestBuilders.delete("/public/api/v1/delete")
                .param("fileId", "id0000")
                .param("fileType", "CHECK"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(aws, Mockito.times(1)).deleteObject(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
        );
    }
}
