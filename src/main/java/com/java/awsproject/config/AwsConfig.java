package com.java.awsproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class AwsConfig {

    @Value("${aws.region:us-east-1}")
    private String region;

    @Bean
    public DefaultCredentialsProvider credentialsProvider() {
        return DefaultCredentialsProvider.create();
    }

    @Bean
    public S3Client s3Client(DefaultCredentialsProvider credentialsProvider) {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    public SnsClient snsClient(DefaultCredentialsProvider credentialsProvider) {
        return SnsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    public DynamoDbClient dynamoDbClient(DefaultCredentialsProvider credentialsProvider) {
        return DynamoDbClient.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }
}
