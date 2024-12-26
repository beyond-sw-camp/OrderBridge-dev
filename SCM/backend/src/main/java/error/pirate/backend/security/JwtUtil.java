package error.pirate.backend.security;

import error.pirate.backend.user.command.application.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.tuple.Pair;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final UserService userService;
    private final String jwtSecretKey;
    private final Long accessExpiration;
    private final Long refreshExpiration;
    private final String accessHeader;
    private final String refreshHeader;

    public JwtUtil(UserService userService,
                   @Value("${jwt.secretKey}") String jwtSecretKey,
                   @Value("${jwt.access.expiration}") Long accessExpiration,
                   @Value("${jwt.refresh.expiration}") Long refreshExpiration,
                   @Value("${jwt.access.header}") String accessHeader,
                   @Value("${jwt.refresh.header}") String refreshHeader) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userService = userService;
        this.jwtSecretKey = jwtSecretKey;
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
        this.accessHeader = accessHeader;
        this.refreshHeader = refreshHeader;
    }

    public boolean validateAccessToken(String accessToken) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken); // 백엔드에서 발행한 token이 맞는지 key를 통해 확인
            return true; // Exception이 발생하지 않으면 true 반환
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid Jwt Token {}", e.getMessage()); // Jwt Token 값이 올바르지 않을 경우
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported Jwt Token {}", e.getMessage()); // 지원하지 않는 Jwt Token
        } catch (IllegalArgumentException e) {
            log.error("Jwt Token claims empty {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.info("Jwt Token Expired");
            return true;
        }

        return false;
    }

    public Pair<String, String> validateExpiredAccessToken(String accessToken, String refreshToken) {
        Pair<String, String> pair;
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken); // 백엔드에서 발행한 token이 맞는지 key를 통해 확인

            String refreshStatus = validateRefreshToken(refreshToken);
            if("REFRESH_SUCCESS".equals(refreshStatus)) {
                // accessToken과 refreshToken이 유효한 경우
                pair = Pair.of("ACCESS_SUCCESS", null);
            } else {
                // accessToken은 유효하나 refreshToken이 유효하지 않은 경우 new refreshToken 발급
                pair = Pair.of(refreshStatus, createRefreshToken());
            }
        } catch (ExpiredJwtException e) {

            String refreshStatus = validateRefreshToken(refreshToken);
            if("REFRESH_SUCCESS".equals(refreshStatus)) {
                // accessToken은 유효하지 않지만 refreshToken이 유효한 경우 accessToken 새로 발급
                pair = Pair.of("ACCESS_EXPIRED", createAccessToken(getAuthentication(accessToken)));
            } else {
                // accessToken도 유효하지 않고 refreshToken도 유효하지 않은 경우
                log.error("Expired Jwt Token {}", e.getMessage()); // Jwt Token이 만료된 경우
                pair = Pair.of("ACCESS_FAIL", null);
            }
        }
        return pair;
    }

    public String validateRefreshToken(String refreshToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);

            return "REFRESH_SUCCESS";
        } catch (ExpiredJwtException e) {
            // Refresh Token은 만료가 아니면 오류 throw하도록 설정
            log.info("AccessToken is Valid But Jwt RefreshToken is Expired new Jwt RefreshToken");
        }
        return "REFRESH_EXPIRED";
    }

    /* Token에서 Claims 추출 */
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /* Token에서 사용자의 id(subject 클레임) 추출 */
    public String getUserEmployeeNo(String token) {
        return parseClaims(token).getSubject();
    }

    public Authentication getAuthentication(String token) {
        // 요청 시 Jwt Token을 이용해 userId 값을 구해 UserDetails 객체를 얻어온다.
        UserDetails user = userService.loadUserByUsername(this.getUserEmployeeNo(token));

        Claims claims = parseClaims(token);

        if(claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    public String createAccessToken(Authentication authentication) {

        /* 권한을 꺼내 List<String>으로 변환 */
        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        /* Jwt Token에 들어갈 Claim 생성 */
        Claims claims = Jwts.claims().setSubject(authentication.getName()); // subject로 Id(userEmployeeNo) 값 지정
        claims.put("auth", authorities);

        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createRefreshToken() {
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));

        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * AccessToken 헤더 설정
     */
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    /**
     * RefreshToken 헤더 설정
     */
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }
}
