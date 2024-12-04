package error.pirate.backend.purchaseOrder.command.domain.aggregate.entity;

import error.pirate.backend.client.command.domain.aggregate.entity.Client;
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
@Table(name = "tb_purchase_order") // 발주서
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseOrder {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long purchaseOrderSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user; // 발주서 담당자

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "clientSeq")
    private Client client; // 거래처

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "salesOrderSeq")
    private SalesOrder salesOrder; // 주문서

    private String purchaseOrderName; // 발주서 명

    private PurchaseOrderStatus purchaseOrderStatus; // 발주서 상태

    @CreatedDate
    private LocalDateTime purchaseOrderRegDate; // 발주서 등록일

    @Column(insertable = false)
    @LastModifiedDate
    private LocalDateTime purchaseOrderModDate; // 발주서 수정일

    private LocalDateTime purchaseOrderDueDate; // 발주서 계약 납기일

    private LocalDateTime purchaseOrderTargetDueDate; // 발주서 목표 납기일

    private Integer purchaseOrderExtendedPrice; // 발주서 총금액

    private String purchaseOrderNote; // 발주서 비고
}
