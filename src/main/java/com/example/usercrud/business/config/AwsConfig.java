package com.example.usercrud.business.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AwsConfig {
    @Bean
    public AmazonS3 getAmazonS3(){
        AWSCredentials credentials = new BasicAWSCredentials(
            "YCAJE6Wt80x6e9hpi6z71HTVJ",
            "YCOJMZZ3JS_uuicZibtB3X2A_6LFp5hW9VVawXaq"
        );

        return AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withEndpointConfiguration(
                new AmazonS3ClientBuilder.EndpointConfiguration(
                    "storage.yandexcloud.net","ru-central1"
                )
            )
            .build();
    }
}
