package error.pirate.backend.client.command.domain.aggregate.entity;

import error.pirate.backend.user.command.domain.aggregate.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_client") // 거래처
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Client {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user; // 거래처 등록 회원

    private String clientRegistrationNo; // 거래처 사업자 등록번호

    private String clientName; // 거래처 이름

    private String clientPhoneNo; // 거래처 전화번호

    private String clientEmail; // 거래처 이메일

    @CreatedDate
    private LocalDateTime clientRegDate; // 거래처 등록일

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime clientModDate; // 거래처 수정일
}
