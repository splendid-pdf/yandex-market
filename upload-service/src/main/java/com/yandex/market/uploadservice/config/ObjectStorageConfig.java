package com.yandex.market.uploadservice.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.yandex.market.uploadservice.config.properties.ObjectStorageProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectStorageConfig {


    @Bean
    public AmazonS3 objectStorage(ObjectStorageProperties properties) {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(properties.getCredentials()))
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                properties.getHost(),
                                properties.getRegion()
                        )
                )
                .build();
    }

}
