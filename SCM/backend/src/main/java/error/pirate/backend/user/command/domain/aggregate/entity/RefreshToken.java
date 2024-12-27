package error.pirate.backend.user.command.domain.aggregate.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refreshToken", timeToLive = 1209600000)
@ToString
public class RefreshToken {
    @Id
    private String userEmployeeNo;

    @Indexed
    private String refreshToken;

    private LocalDateTime createdAt;

    public RefreshToken(String userEmployeeNo, String refreshToken) {
        this.userEmployeeNo = userEmployeeNo;
        this.refreshToken = refreshToken;
        this.createdAt = LocalDateTime.now();
    }
}
