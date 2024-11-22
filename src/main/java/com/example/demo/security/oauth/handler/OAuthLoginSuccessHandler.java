package com.example.demo.security.oauth.handler;

import com.example.demo.base.code.status.exception.GeneralException;
import com.example.demo.base.code.status.exception.ErrorStatus;
import com.example.demo.domain.enums.Role;
import com.example.demo.domain.token.RefreshToken;
import com.example.demo.domain.user.User;
import com.example.demo.repository.token.RefreshTokenRepository;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.security.oauth.GoogleUserInfo;
import com.example.demo.security.oauth.KakaoUserInfo;
import com.example.demo.security.oauth.NaverUserInfo;
import com.example.demo.security.oauth.OAuth2UserInfo;
import com.example.demo.util.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    // 생성자에서 값을 받아옴
    private final String redirectUriTemplate;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;

    // 생성자 주입으로 외부 설정 값을 받기
    public OAuthLoginSuccessHandler(JwtUtil jwtUtil,
                                    UserRepository userRepository,
                                    RefreshTokenRepository refreshTokenRepository,
                                    @Value("${spring.jwt.redirect}") String redirectUriTemplate,
                                    @Value("${spring.jwt.access-token.expiration-time}") long accessTokenExpirationTime,
                                    @Value("${spring.jwt.refresh-token.expiration-time}") long refreshTokenExpirationTime) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.redirectUriTemplate = redirectUriTemplate;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String provider = token.getAuthorizedClientRegistrationId();

        OAuth2UserInfo oAuth2UserInfo;
        try {
            // OAuth provider에 따라 적절한 사용자 정보를 처리하는 로직
            oAuth2UserInfo = switch (provider) {
                case "google" -> new GoogleUserInfo(token.getPrincipal().getAttributes()); // Google의 경우 attributes를 그대로 전달
                case "kakao" -> new KakaoUserInfo(token.getPrincipal().getAttributes()); // Kakao도 마찬가지로 attributes를 전달
                case "naver" -> {
                    // 네이버의 경우 'response'라는 키를 통해 데이터를 받아오므로 Map으로 캐스팅
                    Map<String, Object> naverResponse = (Map<String, Object>) token.getPrincipal().getAttributes().get("response");
                    yield new NaverUserInfo(naverResponse); // NaverUserInfo에 'response' 데이터 전달
                }
                default -> throw new GeneralException(ErrorStatus.UNSUPPORTED_OAUTH_PROVIDER.getReasonHttpStatus()); // 지원되지 않는 OAuth provider는 예외 발생
            };
        } catch (Exception e) {
            // OAuth 처리 중 오류가 발생했을 때 예외 처리
            throw new GeneralException(ErrorStatus.OAUTH_PROCESSING_FAILED.getReasonHttpStatus());
        }

        User user = userRepository.findByProviderId(oAuth2UserInfo.getProviderId())
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .userId(UUID.randomUUID())
                            .name(oAuth2UserInfo.getName())
                            .provider(provider)
                            .providerId(oAuth2UserInfo.getProviderId())
                            .role(Role.USER)
                            .build();
                    userRepository.save(newUser);
                    return newUser;
                });

        refreshTokenRepository.deleteByUserId(user.getUserId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId(), refreshTokenExpirationTime);
        refreshTokenRepository.save(new RefreshToken(user.getUserId(), refreshToken));

        // 액세스 토큰 대신 사용자 UUID만 전달
        String redirectUri = String.format(redirectUriTemplate, URLEncoder.encode(user.getName(), "UTF-8"), user.getUserId().toString());

        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }
}