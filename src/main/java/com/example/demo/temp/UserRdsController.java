package com.example.demo.temp;

import com.example.demo.base.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserRdsController {
    private final UserRdsService userRdsService;

    public UserRdsController(UserRdsService userRdsService) {
        this.userRdsService = userRdsService;
    }

    // 회원 이름으로 조회
    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByUsername(@PathVariable String username) {
        UserResponse userResponse = userRdsService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponse.onSuccess(userResponse));
    }

    // 새로운 회원 추가
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> addUser(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userRdsService.addUser(userRequest);
        return ResponseEntity.ok(ApiResponse.onSuccess(userResponse));
    }
}