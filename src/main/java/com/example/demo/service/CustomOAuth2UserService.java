package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 카카오 계정 정보 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttribute("kakao_account");
        String email = kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
        String nickname = (String) ((Map<String, Object>) oAuth2User.getAttribute("properties")).get("nickname");

        // 사용자 정보가 없으면 새로 생성
        User user = userRepository.findByEmail(email); // 이메일로 사용자 찾기
        if (user == null) {
            user = new User();
            user.setNickname(nickname); // 사용자 닉네임 설정
            user.setEmail(email); // 사용자 이메일 설정
        }

        // 사용자 정보 저장 (필요에 따라 추가 설정)
        userRepository.save(user);

        return oAuth2User;
    }
}