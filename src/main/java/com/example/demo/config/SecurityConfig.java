package com.example.demo.config;

import com.example.demo.filter.JwtAuthenticationFilter;
import com.example.demo.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(); // JWT 인증 필터 빈 등록
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/oauth2/**").permitAll() // OAuth2 관련 요청은 허용
                        .requestMatchers("/api/user/info").authenticated() // 사용자 정보 API는 인증 필요
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // JWT 필터를 UsernamePasswordAuthenticationFilter 이전에 추가
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/login/oauth2/code/kakao", true) // 로그인 성공 시 리다이렉트할 URL
                        .userInfoEndpoint(userInfo -> userInfo.userService(new CustomOAuth2UserService())) // 사용자 정보 서비스 설정
                );

        return http.build(); // SecurityFilterChain 반환
    }
}