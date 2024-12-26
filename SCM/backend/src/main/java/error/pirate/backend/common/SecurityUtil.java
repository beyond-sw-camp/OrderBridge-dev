package error.pirate.backend.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class SecurityUtil {

    // 현재 인증된 사용자의 UserDetails 반환
    public Optional<UserDetails> getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return Optional.of((UserDetails) authentication.getPrincipal());
        }

        return Optional.empty();
    }

    // 현재 인증된 사용자의 userEmployeeNo 반환
    public String getCurrentUserEmployeeNo() {
        return getCurrentUserDetails()
                .map(UserDetails::getUsername)
                .orElse(null); // 인증된 사용자가 없을 경우, null 반환
    }
}
