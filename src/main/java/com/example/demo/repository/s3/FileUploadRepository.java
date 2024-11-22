package com.example.demo.repository.s3;

import com.example.demo.entity.s3.FileUploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileUploadRepository extends JpaRepository<FileUploadEntity, String> {
    // 사용자 토큰과 폴더 이름으로 파일 URL 조회
    List<FileUploadEntity> findByUserTokenAndFolderName(String userToken, String folderName);
}