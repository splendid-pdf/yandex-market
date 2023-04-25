package com.yandex.market.uploadservice.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.yandex.market.uploadservice.UploadIntegrationTest;
import com.yandex.market.uploadservice.model.FileMetaInfo;
import com.yandex.market.uploadservice.repository.FileMetaInfoRepository;
import org.apache.commons.codec.digest.MurmurHash3;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class FileControllerTest extends UploadIntegrationTest {

    @MockBean
    private AmazonS3 amazonS3;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void test() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/public/api/v1/files")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .queryParam("filesIds", "33f31181-d61d-4b7f-b4ae-9efa76f80a63")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(uuid.toString()));
    }

    private JwtAuthenticationToken getToken() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("kid", "fe0d2248-137f-4810-b66b-e6d9b05213b5")
                .header("alg", "RS256")
                .claim("sub", "arkady@gmail.com")
                .claim("seller-id", "0cd635af-2f23-4861-8441-5ebbbe4ae54a")
                .claim("authorities", List.of("ROLE_SELLER"))
                .build();
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_SELLER");
        return new JwtAuthenticationToken(jwt, authorities);
    }

}