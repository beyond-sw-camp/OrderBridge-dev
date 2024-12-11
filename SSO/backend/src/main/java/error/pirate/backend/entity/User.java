package error.pirate.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userSeq;

    private String userId;
    private String userPwd;
    private String userEmail;
    private String userName;

    @CreatedDate
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime modDate;

    @Builder
    protected User(String userId, String userPwd, String userEmail, String userName) {
        this.userId = userId;
        this.userPwd = userPwd;
        this.userEmail = userEmail;
        this.userName = userName;
    }
}