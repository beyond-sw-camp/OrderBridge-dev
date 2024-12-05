package error.pirate.backend.config;

import error.pirate.backend.security.*;
import error.pirate.backend.security.filter.CustomAuthenticationFilter;
import error.pirate.backend.security.filter.JwtFilter;
import error.pirate.backend.service.UserService;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final Environment env;
    private final JwtUtil jwtUtil;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(
                                    new AntPathRequestMatcher("/login", "POST"),
                                    new AntPathRequestMatcher("/swagger")).permitAll() // /users/ 하위의 모든 POST 요청은 모든 사용자가 접근할 수 있다.
                            .requestMatchers(new AntPathRequestMatcher("/**"))
                            .authenticated(); // 위의 요청 외에는 인증만 필요하다.
                })
                /* session 로그인 방식을 사용하지 않음(JWT Token 방식을 사용할 예정) */
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 커스텀 로그인 필터 이전에 JWT 토큰 확인 필터를 설정
        http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        // Exception 발생 시 핸들러 추가
        http.exceptionHandling(
                exceptionHandling -> {
                    exceptionHandling.accessDeniedHandler(new JwtAccessDeniedHandler());
                    exceptionHandling.authenticationEntryPoint(new JwtAuthenticationEntryPoint());
                }
        );
        return http.build();
    }

    private Filter getAuthenticationFilter() {

        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationManager(getAuthenticationManager());
        filter.setAuthenticationSuccessHandler(new LoginSuccessHandler(env));
        filter.setAuthenticationFailureHandler(new LoginFailureHandler());

        return filter;
    }

    private AuthenticationManager getAuthenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder); // BcryptPasswordEncoder를 사용하여 비밀번호 인코딩 하도록 지정
        provider.setUserDetailsService(userService);  // userService를 UserDeatilService로 지정

        return new ProviderManager(provider);
    }
}