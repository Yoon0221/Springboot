package com.example.demo.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    // 카카오 로그인 성공 시 사용자 닉네임 반환
    @GetMapping("/login/oauth2/code/kakao")
    public String loginSuccess(OAuth2AuthenticationToken authentication) {
        Map<String, Object> attributes = (Map<String, Object>) authentication.getPrincipal().getAttributes();
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        String nickname = (String) properties.get("nickname");

        return "로그인 성공: " + nickname; // 로그인 성공 메시지 반환
    }
}