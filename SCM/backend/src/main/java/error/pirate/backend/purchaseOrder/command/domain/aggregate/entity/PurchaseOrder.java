package error.pirate.backend.purchaseOrder.command.domain.aggregate.entity;

import error.pirate.backend.client.command.domain.aggregate.entity.Client;
import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderUpdateRequest;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_purchase_order") // 발주서
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PurchaseOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private String purchaseOrderName; // 발주서명

    @Enumerated(EnumType.STRING)
    private PurchaseOrderStatus purchaseOrderStatus; // 발주서 상태

    @CreatedDate
    private LocalDateTime purchaseOrderRegDate; // 발주서 등록일

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime purchaseOrderModDate; // 발주서 수정일

    private LocalDateTime purchaseOrderDueDate; // 발주서 계약 납기일

    private LocalDateTime purchaseOrderTargetDueDate; // 발주서 목표 납기일

    private Integer purchaseOrderExtendedPrice; // 발주서 총금액

    private String purchaseOrderNote; // 발주서 비고

    private Integer purchaseOrderTotalQuantity;

    public void changePurchaseOrderTotalQuantity(Integer quantity) {
        this.purchaseOrderTotalQuantity = quantity;
    }

    public void changePurchaseOrderName(String name) {
        this.purchaseOrderName = name;
    }

    public void updatePurchaseOrder(PurchaseOrderUpdateRequest request) {
        this.purchaseOrderDueDate = request.getPurchaseOrderDueDate();
        this.purchaseOrderTargetDueDate = request.getPurchaseOrderTargetDueDate();
        this.purchaseOrderExtendedPrice = request.getPurchaseOrderExtendedPrice();
        this.purchaseOrderNote = request.getPurchaseOrderNote();
    }

    public void changePurchaseOrderStatus(PurchaseOrderStatus status) {
        this.purchaseOrderStatus = status;
    }

    public void objectInjection(User user, Client client, SalesOrder salesOrder) {
        this.user = user;
        this.client = client;
        this.salesOrder = salesOrder;
    }

}
