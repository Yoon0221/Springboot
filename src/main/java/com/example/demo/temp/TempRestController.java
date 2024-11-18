package com.example.demo.temp;

import com.example.demo.base.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temp")
@RequiredArgsConstructor
public class TempRestController {

    private final TempQueryService tempQueryService;

    // 비즈니스 로직을 처리하는 GET 요청 API
    @GetMapping("/test")
    public ApiResponse<TempResponse.TempTestDTO> testAPI() {
        return ApiResponse.onSuccess(TempConverter.toTempTestDTO());
    }

    // 에러를 발생시키는 예외 처리 API
    @GetMapping("/exception")
    public ApiResponse<TempResponse.TempExceptionDTO> exceptionAPI(@RequestParam Integer flag) {
        tempQueryService.checkFlag(flag);
        return ApiResponse.onSuccess(TempConverter.toTempExceptionDTO(flag));
    }
}