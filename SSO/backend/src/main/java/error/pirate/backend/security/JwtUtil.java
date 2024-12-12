package error.pirate.backend.security;

import error.pirate.backend.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final UserService userService;

    public JwtUtil(@Value("${token.secret}") String secret, UserService userService) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userService = userService;
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // 백엔드에서 발행한 token이 맞는지 key를 통해 확인
            return true; // Exception이 발생하지 않으면 true 반환
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid Jwt Token {}", e.getMessage()); // Jwt Token 값이 올바르지 않을 경우
        } catch (ExpiredJwtException e) {
            log.error("Expired Jwt Token {}", e.getMessage()); // Jwt Token이 만료된 경우
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported Jwt Token {}", e.getMessage()); // 지원하지 않는 Jwt Token
        } catch (IllegalArgumentException e) {
            log.error("Jwt Token claims empty {}", e.getMessage());
        }

        return false;
    }

    /* Token에서 Claims 추출 */
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /* Token에서 사용자의 id(subject 클레임) 추출 */
    public String getUserId(String token) {
        return parseClaims(token).getSubject();
    }

    public Authentication getAuthentication(String token) {
        // 요청 시 Jwt Token을 이용해 userId 값을 구해 UserDetails 객체를 얻어온다.
        UserDetails user = userService.loadUserByUsername(this.getUserId(token));

        Claims claims = parseClaims(token);

        if(claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        return new UsernamePasswordAuthenticationToken(user, "(서명)", user.getAuthorities());
    }
}
