package com.example.demo.security.filter;

import com.example.demo.base.code.status.exception.GeneralException;
import com.example.demo.base.code.status.exception.ErrorStatus;
import com.example.demo.util.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@WebFilter("/*")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Swagger UI 경로는 인증 필터를 거치지 않도록 예외 처리
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/swagger-ui/") || requestURI.startsWith("/v3/api-docs") || requestURI.startsWith("/swagger-resources") || requestURI.startsWith("/webjars")) {
            filterChain.doFilter(request, response);  // 인증 필터를 거치지 않음
            return;
        }

        // 로그인, 회원가입 경로는 인증 필터를 거치지 않도록 예외 처리
        if (requestURI.startsWith("/api/login") || requestURI.startsWith("/api/register") || requestURI.startsWith("/login")) {
            filterChain.doFilter(request, response);  // 인증 필터를 거치지 않음
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        try {
            // Authorization 헤더 확인
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new GeneralException(ErrorStatus.INVALID_AUTHORIZATION_HEADER.getReasonHttpStatus());
            }

            // 토큰 추출 및 검증
            String token = jwtUtil.getTokenFromHeader(authorizationHeader);
            if (jwtUtil.isTokenExpired(token)) {
                throw new GeneralException(ErrorStatus.TOKEN_EXPIRED.getReasonHttpStatus());
            }

            // 사용자 ID 추출 및 인증 정보 설정
            String userId = jwtUtil.getUserIdFromToken(token);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (GeneralException e) {
            // 인증 오류 처리
            handleAuthenticationError(response);
            return; // 필터 체인 중단
        } catch (Exception e) {
            // 기타 예외 처리
            throw new GeneralException(ErrorStatus.FETCH_FAILED.getReasonHttpStatus()); // "FETCH_FAILED"로 예외 던지기
        }

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }

    /**
     * 인증 실패 시 에러 응답 처리
     */
    private void handleAuthenticationError(HttpServletResponse response) throws IOException {
        // 인증 관련 예외 처리
        throw new GeneralException(ErrorStatus.INVALID_AUTHORIZATION_HEADER.getReasonHttpStatus());
    }
}