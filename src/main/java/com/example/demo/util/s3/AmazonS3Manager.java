package com.example.demo.util.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
public class AmazonS3Manager {

    private final String awsAccessKey;
    private final String awsSecretKey;
    private final String s3Bucket;
    private final String region;

    private AmazonS3 amazonS3;

    // 생성자: @Value로 주입받은 값을 사용해 초기화
    public AmazonS3Manager(
            @Value("${cloud.aws.credentials.accessKey}") String awsAccessKey,
            @Value("${cloud.aws.credentials.secretKey}") String awsSecretKey,
            @Value("${cloud.aws.s3.bucket}") String s3Bucket,
            @Value("${cloud.aws.region.static}") String region) {

        this.awsAccessKey = awsAccessKey;
        this.awsSecretKey = awsSecretKey;
        this.s3Bucket = s3Bucket;
        this.region = region;

        // 자격 증명을 BasicAWSCredentials를 통해 설정
        AWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

        // AmazonS3 클라이언트 빌더를 사용하여 자격 증명과 리전을 설정
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(region)  // 리전 설정
                .withCredentials(new AWSStaticCredentialsProvider(credentials))  // 자격 증명 설정
                .build();  // S3 클라이언트 빌드
    }

    // 파일 업로드 메서드
    public String uploadFile(String username, String foldername, MultipartFile file) throws IOException {
        // S3에 저장할 파일 이름 구성
        String fileName = username + "/" + foldername + "/" + file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();

        // S3에 파일 업로드
        amazonS3.putObject(new PutObjectRequest(s3Bucket, fileName, inputStream, null));

        // 업로드된 파일의 URL 반환
        return "https://" + s3Bucket + ".s3." + region + ".amazonaws.com/" + fileName;
    }
}
