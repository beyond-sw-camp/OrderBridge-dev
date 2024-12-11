package error.pirate.backend.shippingSlip.command.domain.aggregate.entity;

import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstruction;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_shipping_slip") // 출하전표
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ShippingSlip {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shippingSlipSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "shippingInstructionSeq")
    private ShippingInstruction shippingInstruction; // 출하 지시서

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user; // 출하전표 담당자

    private String shippingSlipName; // 출하전표 명

    private String shippingSlipAddress; // 출하전표 주소

    @CreatedDate
    private LocalDateTime shippingSlipRegDate; // 출하전표 등록일

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime shippingSlipModDate; // 출하전표 수정일

    private LocalDateTime shippingSlipShippingDate; // 출하전표 출하일

    private Integer shippingSlipTotalQuantity; // 출하전표 총 수량

    private String shippingSlipNote; // 출하전표 비고
}
