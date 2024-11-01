package com.example.demo.controller;

import com.example.demo.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/info")
    public String getUserInfo(@AuthenticationPrincipal User user) {
        return "로그인 성공: " + user.getNickname(); // 사용자 닉네임 반환
    }
}