package error.pirate.backend.user.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
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

    private Integer age;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private UserSocialType userSocialType;

    private String userName; // 회원 이름

    private String userPhoneNo; // 회원 핸드폰 번호

    private String userEmail; // 회원 이메일

    private String userSocialId;

    private String refreshToken;

    public void authorizeUser() {
        this.userRole = UserRole.MEMBER;
    }

    public void passwordEncode(BCryptPasswordEncoder passwordEncoder) {
        this.userPwd = passwordEncoder.encode(this.userPwd);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }
}
