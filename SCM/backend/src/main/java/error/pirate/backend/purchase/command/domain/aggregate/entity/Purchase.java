package error.pirate.backend.purchase.command.domain.aggregate.entity;

import error.pirate.backend.purchase.command.application.dto.PurchaseUpdateRequest;
import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrder;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_purchase") // 구매
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Purchase {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseOrderSeq")
    private PurchaseOrder purchaseOrder; // 발주서

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user; // 구매 담당자

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseSeq")
    private Warehouse warehouse; // 입고 창고

    private String purchaseName; // 구매서 명

    private LocalDateTime purchaseContractDate; // 구매 계약일

    @CreatedDate
    private LocalDateTime purchaseRegDate; // 구매서 등록일

    @Column(insertable = false)
    @LastModifiedDate
    private LocalDateTime purchaseModDate; // 구매서 수정일

    @Enumerated(EnumType.STRING)
    private PurchaseStatus purchaseStatus; // 구매 상태

    private Integer purchaseExtendedPrice; // 구매서 총금액

    private Integer purchaseOrderTotalQuantity;  //구매서 총수량

    private String purchaseNote; // 구매서 비고

    public void changePurchaseName(String purchaseName) {
        this.purchaseName = purchaseName;
    }

    public void changePurchaseStatus(PurchaseStatus purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    public void objectInjection(User user, Warehouse warehouse, PurchaseOrder purchaseOrder) {
        this.user = user;
        this.warehouse = warehouse;
        this.purchaseOrder = purchaseOrder;
    }

    public void updatePurchase(PurchaseUpdateRequest request) {
        this.purchaseContractDate = request.getPurchaseContractDate();
        this.purchaseStatus = request.getPurchaseStatus();
        this.purchaseExtendedPrice = request.getPurchaseExtendedPrice();
        this.purchaseNote = request.getPurchaseNote();
    }

}
