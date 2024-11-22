package com.example.demo.controller.s3;

import com.example.demo.base.ApiResponse;
import com.example.demo.base.code.status.exception.ErrorStatus;
import com.example.demo.base.code.status.exception.GeneralException;
import com.example.demo.domain.dto.s3.FileUploadRequestDTO;
import com.example.demo.domain.dto.s3.FileUploadResponseDTO;
import com.example.demo.entity.s3.FileUploadEntity;
import com.example.demo.repository.s3.FileUploadRepository;
import com.example.demo.service.s3.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Operation(summary = "Upload a file", description = "파일을 업로드합니다.",
            parameters = {
                    @Parameter(name = "folderName", description = "업로드할 폴더명"),
                    @Parameter(name = "file", description = "업로드할 파일", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<FileUploadResponseDTO> uploadFile(
            @RequestParam("folderName") String folderName,
            @RequestParam("file") MultipartFile file) {

        try {
            // SecurityContext에서 사용자 ID 추출
            String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // DTO 생성
            FileUploadRequestDTO requestDTO = new FileUploadRequestDTO();
            requestDTO.setUserToken(userId); // 추출한 사용자 ID 설정
            requestDTO.setFolderName(folderName);
            requestDTO.setFile(file);

            // 파일 업로드 서비스 호출
            FileUploadResponseDTO responseDTO = fileUploadService.uploadFile(requestDTO);
            return ApiResponse.onSuccess(responseDTO);
        } catch (Exception e) {
            // 파일 업로드 실패 시 에러 처리
            throw new GeneralException(ErrorStatus.UPLOAD_FAILED.getReasonHttpStatus());
        }
    }

    @GetMapping("/urls")
    public ApiResponse<List<String>> getFileUrls(@RequestParam String folderName) {
        try {
            // SecurityContext에서 사용자 ID 추출
            String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // DB에서 해당 조건에 맞는 파일 URL 조회
            List<FileUploadEntity> fileUploadEntities = fileUploadRepository.findByUserTokenAndFolderName(userId, folderName);

            // 파일이 없으면 에러 처리
            if (fileUploadEntities.isEmpty()) {
                throw new GeneralException(ErrorStatus.NOT_FOUND.getReasonHttpStatus());
            }

            List<String> fileUrls = fileUploadEntities.stream()
                    .map(FileUploadEntity::getFileUrl)
                    .toList();
            return ApiResponse.onSuccess(fileUrls);
        } catch (GeneralException ge) {
            throw ge;
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.FETCH_FAILED.getReasonHttpStatus());
        }
    }
}