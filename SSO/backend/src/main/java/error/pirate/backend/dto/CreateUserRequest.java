package error.pirate.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@AllArgsConstructor
public class CreateUserRequest {
    private String userId;
    private String userPwd;
    private String userName;
    private String userEmail;

    public void changePasswordEncoder(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.userPwd = bCryptPasswordEncoder.encode(password);
    }
}
