package error.pirate.backend.shippingInstruction.command.domain.aggregate.entity;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_shipping_instruction") // 출하 지시서
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShippingInstruction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long shippingInstructionSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "salesOrderSeq")
    private SalesOrder salesOrder; // 주문서

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user; // 출하 지시서 담당자

    private String shippingInstructionAddress;
    
    private String shippingInstructionStatus;
    
    @CreatedDate
    private LocalDateTime shippingInstructionRegDate; // 출하 지시서 등록일

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime shippingInstructionModDate; // 출하 지시서 수정일

    private LocalDateTime shippingInstructionScheduledShipmentDate; // 출하 지시서 출하 예정일
    
    private String shippingInstructionNote; // 출하 지시서 비고
}
