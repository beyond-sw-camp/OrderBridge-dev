package error.pirate.backend.quotation.command.domain.aggregate.entity;

import error.pirate.backend.client.command.domain.aggregate.entity.Client;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_quotation") // 견적서
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quotation {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long quotationSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user; // 견적서 담당자

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "clientSeq")
    private Client client; // 거래처

    private QuotationStatus quotationStatus; // 견적서 상태

    @CreatedDate
    private LocalDateTime quotationRegDate; // 견적서 등록일

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime quotationModDate; // 견적서 수정일

    private LocalDateTime quotationQuotationDate; // 견적서 견적일

    private String quotationNote; // 견적서 비고
}
