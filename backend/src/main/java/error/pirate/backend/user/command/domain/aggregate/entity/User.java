package error.pirate.backend.user.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_user") // 회원
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userSeq;

    private String userName; // 회원 이름

    private String userEmployeeNo; // 회원 사원번호

    private String userPhoneNo; // 회원 핸드폰 번호

    private String userEmail; // 회원 이메일
}
