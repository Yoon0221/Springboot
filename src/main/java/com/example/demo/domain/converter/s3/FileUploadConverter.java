package com.example.demo.domain.converter.s3;

import com.example.demo.domain.dto.s3.FileUploadRequestDTO;
import com.example.demo.domain.dto.s3.FileUploadResponseDTO;
import com.example.demo.entity.s3.FileUploadEntity;

public class FileUploadConverter {

    // FileUploadRequestDTO를 FileUploadEntity로 변환
    public static FileUploadEntity toEntity(FileUploadRequestDTO requestDTO, String fileUrl) {
        return new FileUploadEntity(requestDTO.getUserToken(), requestDTO.getFolderName(), fileUrl);
    }

    // FileUploadEntity를 FileUploadResponseDTO로 변환
    public static FileUploadResponseDTO toResponseDTO(FileUploadEntity entity) {
        return new FileUploadResponseDTO(entity.getFileUrl());
    }
}