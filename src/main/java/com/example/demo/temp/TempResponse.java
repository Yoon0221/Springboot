package com.example.demo.temp;

import lombok.Data;

public class TempResponse {

    @Data
    public static class TempTestDTO {
        private String message;
    }

    @Data
    public static class TempExceptionDTO {
        private Integer flag;
        private String errorMessage;
    }
}