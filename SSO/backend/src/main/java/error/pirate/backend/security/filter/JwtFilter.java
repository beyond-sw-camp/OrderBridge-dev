package error.pirate.backend.security.filter;

import error.pirate.backend.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if(authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);

            if(jwtUtil.validateToken(token)) {
                Authentication authentication = jwtUtil.getAuthentication(token);

                // 인증이 완료된 후 Security에서 관리하는 ContextHolder에 Authentication 값을 저장한다.
                // 인증이 완료되면 이후 인증 필터는 건너 뛰게 된다.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // 위의 if문에 걸리지 않아 Authentication 객체가 설정되지 않으면 다음 필터가 실행된다.
        filterChain.doFilter(request, response);
    }
}