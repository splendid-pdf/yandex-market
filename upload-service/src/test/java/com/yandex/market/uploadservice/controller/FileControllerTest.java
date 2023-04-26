package com.yandex.market.uploadservice.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.yandex.market.uploadservice.UploadIntegrationTest;
import org.apache.commons.codec.digest.MurmurHash3;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static com.yandex.market.auth.util.AuthUtils.token;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class FileControllerTest extends UploadIntegrationTest {

    @MockBean
    private AmazonS3 amazonS3;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getUrlsWhenOk() throws Exception {
        mockGenerateUrlMethod();

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/public/api/v1/files")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .queryParam("filesIds",
                                                  "eb527df9-fac2-4de5-96ea-8c11ba8089f0",
                                                  "2b8e3df0-628a-4ef0-9044-e14a995b539d",
                                                  "cb25d05a-11f0-46c2-bc17-1a1185ade628")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                                                        [
                                                            "https://www.instagram.com/1",
                                                            "https://www.instagram.com/2",
                                                            "https://www.instagram.com/3"
                                                        ]
                                                     """
                ));

        verify(amazonS3, times(3)).generatePresignedUrl(any(), any(), any());
        verifyNoMoreInteractions(amazonS3);
    }

    @Test
    public void uploadWhenOk() throws Exception {
        UUID uuid = UUID.fromString("62dc66f7-e141-4283-9c1d-a0dd0e2aba21");
        List<MockMultipartFile> mockFiles = getMockedFiles();
        mockPutMethod();
        try (
                MockedStatic<UUID> mockedUuid = Mockito.mockStatic(UUID.class);
                MockedStatic<MurmurHash3> mockedMurMur = Mockito.mockStatic(MurmurHash3.class)
        ) {
            mockedUuid.when(UUID::randomUUID).thenReturn(uuid);
            mockedMurMur.when(() -> MurmurHash3.hash32x86(mockFiles.get(0).getBytes())).thenReturn(0);
            mockedMurMur.when(() -> MurmurHash3.hash32x86(mockFiles.get(1).getBytes())).thenReturn(1);
            mockedMurMur.when(() -> MurmurHash3.hash32x86(mockFiles.get(2).getBytes())).thenReturn(2);
            mockedMurMur.when(() -> MurmurHash3.hash32x86(mockFiles.get(3).getBytes())).thenReturn(3);

            mockMvc.perform(
                            MockMvcRequestBuilders.multipart("/public/api/v1/files")
                                    .file(mockFiles.get(0))
                                    .file(mockFiles.get(1))
                                    .file(mockFiles.get(2))
                                    .file(mockFiles.get(3))
                                    .with(authentication(token("t51c4cd3-6fe7-4d3e-b82c-f5d044e46091", "ROLE_USER")))
                                    .accept(MediaType.MULTIPART_FORM_DATA)
                                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("""
                                                            [
                                                              "eb527df9-fac2-4de5-96ea-8c11ba8089f0",
                                                              "2b8e3df0-628a-4ef0-9044-e14a995b539d",
                                                              "cb25d05a-11f0-46c2-bc17-1a1185ade628",
                                                              "62dc66f7-e141-4283-9c1d-a0dd0e2aba21"
                                                            ]
                                                         """
                    ));
        }

        verify(amazonS3, times(3)).generatePresignedUrl(any(), any(), any());
        verifyNoMoreInteractions(amazonS3);
    }

    private List<MockMultipartFile> getMockedFiles() throws IOException {
        MockMultipartFile file0 = new MockMultipartFile(
                "files",
                "test_photo_0.jpeg",
                null,
                Files.readAllBytes(Paths.get("src/test/resources/photos/test_photo_0.jpeg")));

        MockMultipartFile file1 = new MockMultipartFile(
                "files",
                "test_photo_1.jpg",
                null,
                Files.readAllBytes(Paths.get("src/test/resources/photos/test_photo_1.jpg")));

        MockMultipartFile file2 = new MockMultipartFile(
                "files",
                "test_photo_2.png",
                null,
                Files.readAllBytes(Paths.get("src/test/resources/photos/test_photo_2.png")));

        MockMultipartFile file3 = new MockMultipartFile(
                "files",
                "test_photo_3.jpg",
                null,
                Files.readAllBytes(Paths.get("src/test/resources/photos/test_photo_3.jpg")));

        return List.of(file0, file1, file2, file3);
    }

    private void mockGenerateUrlMethod() throws MalformedURLException {
        Mockito.doReturn(new URL("https://www.instagram.com/1"))
                .when(amazonS3)
                .generatePresignedUrl(any(), eq("eb527df9-fac2-4de5-96ea-8c11ba8089f0"), any());

        Mockito.doReturn(new URL("https://www.instagram.com/2"))
                .when(amazonS3)
                .generatePresignedUrl(any(), eq("2b8e3df0-628a-4ef0-9044-e14a995b539d"), any());

        Mockito.doReturn(new URL("https://www.instagram.com/3"))
                .when(amazonS3)
                .generatePresignedUrl(any(), eq("cb25d05a-11f0-46c2-bc17-1a1185ade628"), any());
    }

    private void mockPutMethod() {
        Mockito.doReturn(null)
                .when(amazonS3)
                .putObject(any(), eq("62dc66f7-e141-4283-9c1d-a0dd0e2aba21"), any(), any());
    }

}