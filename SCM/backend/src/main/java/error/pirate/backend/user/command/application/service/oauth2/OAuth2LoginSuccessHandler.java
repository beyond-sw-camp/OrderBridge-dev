package error.pirate.backend.user.command.application.service.oauth2;

import error.pirate.backend.security.JwtUtil;
import error.pirate.backend.user.command.application.dto.oauth2.CustomOAuth2User;
import error.pirate.backend.user.command.domain.aggregate.entity.UserRole;
import error.pirate.backend.user.command.domain.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("oauth2 로그인 성공 principal : {}", authentication);
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            String accessToken = jwtUtil.createAccessToken(authentication);

            jwtUtil.setAccessTokenHeader(response, accessToken);

            if(UserRole.GUEST == oAuth2User.getUserRole()) {
                response.sendRedirect("oauth2/sign-up");
            } else {
                String refreshToken = jwtUtil.createRefreshToken();

                jwtUtil.setRefreshTokenHeader(response, refreshToken);
            }
        } catch (Exception e) {
            log.error("OAuth2LoginSuccess Exception : {}", e.getMessage());
            throw e;
        }
    }
}
