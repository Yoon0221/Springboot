package com.example.demo.base.code.status.exception;

import com.example.demo.base.code.BaseErrorCode;
import com.example.demo.base.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 공통 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러가 발생했습니다. 관리자에게 문의하세요."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "접근이 금지되었습니다."),

    // s3 관련 에러
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE4001", "파일 업로드 실패"),
    FILE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE4002", "파일 저장 실패"),
    UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE4003", "파일 업로드 중 오류가 발생했습니다."),
    FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE4004", "파일 URL 조회 중 오류가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "FILE4005", "사용자 또는 폴더를 찾을 수 없습니다."),

    // SocialLogin
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH4001", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH4002", "토큰이 만료되었습니다."),
    INVALID_AUTHORIZATION_HEADER(HttpStatus.BAD_REQUEST, "AUTH4003", "유효하지 않은 Authorization 헤더입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4001", "사용자를 찾을 수 없습니다."),
    INVALID_PROVIDER(HttpStatus.BAD_REQUEST, "OAUTH4001", "유효하지 않은 OAuth 제공자입니다."),
    OAUTH_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "OAUTH4002", "OAuth 로그인에 실패했습니다."),
    UNSUPPORTED_OAUTH_PROVIDER(HttpStatus.BAD_REQUEST, "OAUTH4003", "지원하지 않는 OAuth 제공자입니다."),
    OAUTH_PROCESSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "OAUTH5001", "OAuth 처리에 실패했습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "TOKEN4001", "리프레시 토큰을 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}