package error.pirate.backend.user.command.application.dto.oauth2;

import error.pirate.backend.user.command.domain.aggregate.entity.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final String userEmployeeNo;
    private final UserRole userRole;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            String nameAttributeKey, String userEmployeeNo, UserRole userRole) {
        super(authorities, attributes, nameAttributeKey);
        this.userEmployeeNo = userEmployeeNo;
        this.userRole = userRole;
    }
}
