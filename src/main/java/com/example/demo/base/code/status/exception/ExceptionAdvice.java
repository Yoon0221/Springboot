package com.example.demo.base.code.status.exception;

import com.example.demo.base.ApiResponse;
import com.example.demo.base.code.ErrorReasonDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    // ConstraintViolationException 처리
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException e, WebRequest request) {
        Map<String, String> errors = e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage(),
                        (msg1, msg2) -> msg1, // 중복 키 발생 시 첫 번째 메시지 사용
                        LinkedHashMap::new
                ));

        log.error("Constraint Violation Errors: {}", errors);

        return buildValidationErrorResponse(ErrorStatus.BAD_REQUEST, errors);
    }

    // MethodArgumentNotValidException 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        // 필드 오류를 Map 형태로 변환
        Map<String, Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> Map.of(
                                "rejectedValue", fieldError.getRejectedValue() != null ? fieldError.getRejectedValue().toString() : "null",
                                "errorMessage", fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Invalid value"
                        ),
                        (map1, map2) -> map1, // 중복 키 발생 시 첫 번째 값 사용
                        LinkedHashMap::new
                ));

        log.error("Validation failed for fields: {}", errors);

        return buildValidationErrorResponse(ErrorStatus.BAD_REQUEST, errors);
    }

    // GeneralException 처리
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(GeneralException e, HttpServletRequest request) {
        ErrorReasonDTO errorReason = e.getErrorReasonHttpStatus();
        log.error("General Exception: {}", errorReason.getMessage(), e);

        return buildErrorResponse(errorReason, request);
    }

    // 기타 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleOtherExceptions(Exception e, WebRequest request) {
        log.error("Unexpected Exception occurred: ", e);

        return buildErrorResponse(ErrorStatus.INTERNAL_SERVER_ERROR, request, e.getMessage());
    }

    // 공통 유효성 검증 에러 응답 빌드 메서드
    private ResponseEntity<ApiResponse<Object>> buildValidationErrorResponse(ErrorStatus errorStatus, Map<?, ?> errors) {
        ApiResponse<Object> response = ApiResponse.onFailure(
                errorStatus.getCode(),
                errorStatus.getMessage(),
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 공통 에러 응답 빌드 메서드 (일반 에러)
    private ResponseEntity<ApiResponse<Object>> buildErrorResponse(ErrorStatus errorStatus, WebRequest request, Object details) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorStatus.getCode(), errorStatus.getMessage(), details);
        return ResponseEntity.status(errorStatus.getHttpStatus()).body(body);
    }

    // 공통 에러 응답 빌드 메서드 (ErrorReasonDTO 기반)
    private ResponseEntity<ApiResponse<Object>> buildErrorResponse(ErrorReasonDTO errorReason, HttpServletRequest request) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorReason.getCode(), errorReason.getMessage(), null);
        return ResponseEntity.status(errorReason.getHttpStatus()).body(body);
    }
}