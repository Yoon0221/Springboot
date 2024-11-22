package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI createOpenAPI() {
        // API 정보 설정
        Info apiInfo = new Info()
                .title("제발 그냥 되라") // API 제목
                .description("UMC Server WorkBook API 명세서") // API 설명
                .version("1.0.0"); // API 버전

        // JWT 인증 설정
        String jwtSchemeName = "JWT_TOKEN";

        // 인증 요구사항 추가
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(jwtSchemeName);

        // SecuritySchemes 등록
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName,
                        new SecurityScheme()
                                .name(jwtSchemeName)
                                .type(SecurityScheme.Type.HTTP) // HTTP 인증 방식
                                .scheme("bearer") // Bearer 방식
                                .bearerFormat("JWT")); // JWT 토큰 형식

        // OpenAPI 객체 생성 및 구성
        return new OpenAPI()
                .addServersItem(new Server().url("/")) // 서버 URL 설정
                .info(apiInfo) // API 정보 추가
                .addSecurityItem(securityRequirement) // 인증 요구사항 추가
                .components(components); // 인증 컴포넌트 추가
    }
}