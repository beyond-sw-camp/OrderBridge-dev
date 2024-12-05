package error.pirate.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    private String regDate;

    @LastModifiedDate
    @Column(insertable = false)
    private String modDate;

    @Builder
    protected User(String userId, String userPwd, String userEmail, String userName) {
        this.userId = userId;
        this.userPwd = userPwd;
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public void encryptPassword(String encodedPwd) {
        this.userPwd = encodedPwd;
    }
}