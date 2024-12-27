package error.pirate.backend.security.filter;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.security.JwtUtil;
import error.pirate.backend.user.command.domain.aggregate.entity.RefreshToken;
import error.pirate.backend.user.command.domain.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final Environment env;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
         String authorization = request.getHeader(env.getProperty("jwt.access.header"));
         log.info("authorization : {}", authorization);
        if(authorization != null && authorization.startsWith("Bearer ")) {
            // AccessToken 추출
            String accessToken = authorization.substring(7);

            if(jwtUtil.validateAccessToken(accessToken)) {
                String authorizationRefresh = request.getHeader(env.getProperty("jwt.refresh.header"));

                if(authorizationRefresh != null && authorizationRefresh.startsWith("Bearer ")) {
                    // refreshToken 추출
                    String refreshToken = authorizationRefresh.substring(7);

                    // accessToken, refreshToken 만료 여부 체크
                    Pair<String, String> pair = jwtUtil.validateExpiredAccessToken(accessToken, refreshToken);

                    if(!"ACCESS_FAIL".equals(pair.getLeft())) {
                        if ("ACCESS_EXPIRED".equals(pair.getLeft())) {
                            jwtUtil.setAccessTokenHeader(response, pair.getRight());
                        } else if ("REFRESH_EXPIRED".equals(pair.getLeft())) {
                            jwtUtil.setRefreshTokenHeader(response, pair.getRight());
                        }

                        String userEmployeeNo = jwtUtil.getUserEmployeeNo(accessToken);
                        // refreshToken이 DB에 저장되어 있는 값과 동일한 지 여부 체크
                        RefreshToken redisRefreshToken =  refreshTokenRepository.findById(userEmployeeNo)
                                .orElseThrow(() -> new CustomException(ErrorCodeType.SECURITY_TOKEN_ERROR));
                        if(refreshToken.equals(redisRefreshToken.getRefreshToken())) {

                            Authentication authentication = jwtUtil.getAuthentication(accessToken);

                            // 인증이 완료된 후 Security에서 관리하는 ContextHolder에 Authentication 값을 저장한다.
                            // 인증이 완료되면 이후 인증 필터는 건너 뛰게 된다.
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                }
            }
        }
        // 위의 if문에 걸리지 않아 Authentication 객체가 설정되지 않으면 다음 필터가 실행된다.
        filterChain.doFilter(request, response);
    }
}