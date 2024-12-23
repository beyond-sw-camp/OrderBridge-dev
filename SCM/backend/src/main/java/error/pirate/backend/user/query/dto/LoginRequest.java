package error.pirate.backend.user.query.dto;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String userEmployeeNo;
    private String userPwd;
}
