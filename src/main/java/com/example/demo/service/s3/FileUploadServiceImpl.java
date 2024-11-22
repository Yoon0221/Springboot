package com.example.demo.service.s3;

import com.example.demo.base.code.status.exception.GeneralException;
import com.example.demo.base.code.status.exception.ErrorStatus;
import com.example.demo.domain.converter.s3.FileUploadConverter;
import com.example.demo.domain.dto.s3.FileUploadRequestDTO;
import com.example.demo.domain.dto.s3.FileUploadResponseDTO;
import com.example.demo.entity.s3.FileUploadEntity;
import com.example.demo.repository.s3.FileUploadRepository;
import com.example.demo.util.s3.AmazonS3Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Autowired
    private AmazonS3Manager amazonS3Manager;

    @Override
    public FileUploadResponseDTO uploadFile(FileUploadRequestDTO requestDTO) {
        MultipartFile file = requestDTO.getFile();
        String fileUrl;

        try {
            // 파일을 Amazon S3에 업로드
            fileUrl = amazonS3Manager.uploadFile(requestDTO.getUserToken(), requestDTO.getFolderName(), file);
        } catch (IOException e) {
            // 파일 업로드 실패 시 에러 발생
            throw new GeneralException(ErrorStatus.FILE_UPLOAD_FAILED.getReasonHttpStatus());
        }

        FileUploadEntity fileUploadEntity;
        try {
            // 엔티티 변환
            fileUploadEntity = FileUploadConverter.toEntity(requestDTO, fileUrl);

            // 업로드된 파일 URL을 DB에 저장
            fileUploadRepository.save(fileUploadEntity);
        } catch (Exception e) {
            // DB 저장 실패 시 에러 발생
            throw new GeneralException(ErrorStatus.FILE_SAVE_FAILED.getReasonHttpStatus());
        }

        // 응답 DTO 생성 후 반환
        return FileUploadConverter.toResponseDTO(fileUploadEntity);
    }
}