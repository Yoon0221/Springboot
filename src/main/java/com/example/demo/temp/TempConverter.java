package com.example.demo.temp;

public class TempConverter {

    public static TempResponse.TempTestDTO toTempTestDTO() {
        TempResponse.TempTestDTO dto = new TempResponse.TempTestDTO();
        dto.setMessage("Test message");
        return dto;
    }

    public static TempResponse.TempExceptionDTO toTempExceptionDTO(Integer flag) {
        TempResponse.TempExceptionDTO dto = new TempResponse.TempExceptionDTO();
        dto.setFlag(flag);
        dto.setErrorMessage("Exception occurred for flag: " + flag);
        return dto;
    }
}