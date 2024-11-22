package com.example.demo.entity.s3;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.UUID;

@Entity
public class FileUploadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // UUID로 자동 생성
    private UUID id; // 파일의 고유한 UUID
    private String userToken; // 파일 업로드한 사용자 토큰
    private String folderName; // 파일이 저장된 폴더 이름
    private String fileUrl;   // 파일의 URL

    // 파라미터를 받는 생성자
    public FileUploadEntity(String userToken, String folderName, String fileUrl) {
        this.userToken = userToken;
        this.folderName = folderName;
        this.fileUrl = fileUrl;
    }

    // 기본 생성자
    public FileUploadEntity() {
    }

    // Getter와 Setter 메소드
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}