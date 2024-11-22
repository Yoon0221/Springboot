package com.example.demo.service.s3;

import com.example.demo.domain.dto.s3.FileUploadRequestDTO;
import com.example.demo.domain.dto.s3.FileUploadResponseDTO;

import java.io.IOException;

public interface FileUploadService {

    // 파일 업로드 메소드
    FileUploadResponseDTO uploadFile(FileUploadRequestDTO requestDTO) throws IOException;
}