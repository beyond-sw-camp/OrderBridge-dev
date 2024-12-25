package error.pirate.backend.user.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "tb_user") // 회원
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    private String userEmployeeNo; // 회원 사원번호 -> 아이디

    private String userPwd;

    private String userProfileImgUrl;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private String userName; // 회원 이름

    private String userPhoneNo; // 회원 핸드폰 번호

    private String userEmail; // 회원 이메일

    public void authorizeUser() {
        this.userRole = UserRole.MEMBER;
    }

    public void passwordEncode(BCryptPasswordEncoder passwordEncoder) {
        this.userPwd = passwordEncoder.encode(this.userPwd);
    }

    public static User createUser(String userEmployeeNo,
                                  String userPwd,
                                  String userProfileImgUrl,
                                  UserRole userRole,
                                  String userName,
                                  String userPhoneNo,
                                  String userEmail) {

        return new User(userEmployeeNo, userPwd, userProfileImgUrl,
                userRole, userName, userPhoneNo, userEmail);
    }

    protected User(String userEmployeeNo, String userPwd, String userProfileImgUrl, UserRole userRole, String userName, String userPhoneNo, String userEmail) {
        this.userEmployeeNo = userEmployeeNo;
        this.userPwd = userPwd;
        this.userProfileImgUrl = userProfileImgUrl;
        this.userRole = userRole;
        this.userName = userName;
        this.userPhoneNo = userPhoneNo;
        this.userEmail = userEmail;
    }
}
