package com.example.demo.base.code.status.exception;

import com.example.demo.base.code.ErrorReasonDTO;

public class GeneralException extends RuntimeException {

    private final ErrorReasonDTO errorReason;

    public GeneralException(ErrorReasonDTO errorReason) {
        super(errorReason.getMessage());
        this.errorReason = errorReason;
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.errorReason;
    }
}