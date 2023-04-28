package com.yandex.market.uploadservice;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
public abstract class UploadIntegrationTest {

    //todo: need to create mongo-init.js file and init mongo db. I can't do it
    @Container
    static final MongoDBContainer MONGO_DB_CONTAINER =
            new MongoDBContainer(DockerImageName.parse("mongo:5.0.16-focal"));
                    //.withCopyFileToContainer(MountableFile.forClasspathResource("/mongo-init.js"), "/docker-entrypoint-initdb.d/mongo-init.js:ro");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO_DB_CONTAINER::getReplicaSetUrl);
    }

}

