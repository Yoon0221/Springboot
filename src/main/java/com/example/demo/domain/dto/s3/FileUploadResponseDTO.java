package com.example.demo.domain.dto.s3;

public class FileUploadResponseDTO {

    private String fileUrl;

    public FileUploadResponseDTO(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    // Getter and Setter
    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}