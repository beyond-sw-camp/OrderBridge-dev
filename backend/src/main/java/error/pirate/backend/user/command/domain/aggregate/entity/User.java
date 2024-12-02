package error.pirate.backend.user.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userSeq;

    private String userName;
    private String userEmployeeNo;
    private String userPhoneNo;
    private String userEmail;
}
