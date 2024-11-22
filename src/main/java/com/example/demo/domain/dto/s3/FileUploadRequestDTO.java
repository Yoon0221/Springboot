package com.example.demo.domain.dto.s3;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadRequestDTO {

    private String userToken;  // 사용자 토큰
    private String folderName; // 폴더 이름
    private MultipartFile file;

    // Getters and Setters
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

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}