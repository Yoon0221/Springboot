package com.example.demo.util.jwt;

import com.example.demo.base.code.status.exception.GeneralException;
import com.example.demo.base.code.status.exception.ErrorStatus;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtil {

    private final String secretKey;

    public JwtUtil(@Value("${spring.jwt.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(UUID userId, long expirationMillis) {
        log.info("액세스 토큰이 발행되었습니다.");
        return Jwts.builder()
                .claim("userId", userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(UUID userId, long expirationMillis) {
        log.info("리프레쉬 토큰이 발행되었습니다.");
        return Jwts.builder()
                .claim("userId", userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(getSigningKey())
                .compact();
    }

    public String getTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new GeneralException(ErrorStatus.INVALID_AUTHORIZATION_HEADER.getReasonHttpStatus());
        }
        return authorizationHeader.substring(7);
    }

    public String getUserIdFromToken(String token) {
        try {
            // JwtParser를 빌드하고 parseSignedClaims() 사용
            return Jwts.parser()
                    .setSigningKey(getSigningKey())  // 서명 키 설정
                    .build()  // JwtParser 빌드
                    .parseSignedClaims(token)  // parseClaimsJws 대신 사용
                    .getBody()
                    .get("userId", String.class);  // "userId" 클레임 가져오기
        } catch (JwtException e) {
            log.warn("유효하지 않은 토큰입니다.");
            throw new GeneralException(ErrorStatus.INVALID_TOKEN.getReasonHttpStatus());
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            // JwtParser를 빌드하고 parseSignedClaims() 사용
            Date expirationDate = Jwts.parser()
                    .setSigningKey(getSigningKey())  // 서명 키 설정
                    .build()  // JwtParser 빌드
                    .parseSignedClaims(token)  // parseClaimsJws 대신 사용
                    .getBody()
                    .getExpiration();  // 만료 날짜 가져오기
            return expirationDate.before(new Date());  // 만료일이 현재 시간 이전이면 true 반환
        } catch (JwtException e) {
            log.warn("유효하지 않은 토큰입니다.");
            throw new GeneralException(ErrorStatus.INVALID_TOKEN.getReasonHttpStatus());
        }
    }

}