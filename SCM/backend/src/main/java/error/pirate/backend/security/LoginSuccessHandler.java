package error.pirate.backend.security;

import error.pirate.backend.user.command.domain.aggregate.entity.RefreshToken;
import error.pirate.backend.user.command.domain.repository.RefreshTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("로그인 성공 후 security가 관리하는 principal 객체 : {}", authentication);

        String accessToken = jwtUtil.createAccessToken(authentication);

        // 만료기간이 긴 refreshToken은 탈취되더라도 어떠한 정보도 얻을 수 없게끔 만료기한만 갖도록 설정
        String refreshToken = jwtUtil.createRefreshToken();

        RefreshToken redisRefreshToken = new RefreshToken(authentication.getName(), refreshToken);

        refreshTokenRepository.save(redisRefreshToken);

        jwtUtil.setAccessTokenHeader(response, accessToken);
        jwtUtil.setRefreshTokenHeader(response, refreshToken);
    }
}
