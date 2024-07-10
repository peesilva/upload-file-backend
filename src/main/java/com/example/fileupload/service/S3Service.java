package com.example.fileupload.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody; // Importe esta classe
import java.io.IOException;
import java.io.InputStream;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(@Value("${aws.region}") String awsRegion,
                     @Value("${aws.accessKeyId}") String awsAccessKeyId,
                     @Value("${aws.secretKey}") String awsSecretKey,
                     @Value("${aws.s3.bucketName}") String bucketName) {

        AwsCredentials credentials = AwsBasicCredentials.create(awsAccessKeyId, awsSecretKey);

        this.s3Client = S3Client.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        this.bucketName = bucketName;
    }

    public void uploadFile(String key, MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream()) {
            // Criar RequestBody a partir do InputStream do arquivo
            RequestBody requestBody = RequestBody.fromInputStream(is, file.getSize());

            // Construir o objeto de requisição para enviar para o S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            // Enviar o arquivo para o S3
            s3Client.putObject(putObjectRequest, requestBody);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file.", e);
        }
    }
}