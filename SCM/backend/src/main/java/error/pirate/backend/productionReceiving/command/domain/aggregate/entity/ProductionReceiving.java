package error.pirate.backend.productionReceiving.command.domain.aggregate.entity;

import error.pirate.backend.common.NullCheck;
import error.pirate.backend.productionReceiving.command.application.dto.ProductionReceivingCreateRequest;
import error.pirate.backend.productionReceiving.command.application.dto.ProductionReceivingUpdateRequest;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_production_receiving") // 생산 입고
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tb_production_receiving SET production_receiving_status = 'DELETE' WHERE production_receiving_seq = ? AND production_receiving_status = 'BEFORE'")
public class ProductionReceiving {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productionReceivingSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user; // 생산 입고 관리자

    private String productionReceivingName; // 생산 입고명

    private LocalDateTime productionReceivingReceiptDate;

    @CreatedDate
    private LocalDateTime productionReceivingRegDate; // 생산 입고 등록일

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime productionReceivingModDate; // 생산 입고 수정일

    private Integer productionReceivingExtendedPrice; // 생산 입고 총금액

    private String productionReceivingNote; // 생산 입고 비고

    @Enumerated(value = EnumType.STRING)
    private ProductionReceivingStatus productionReceivingStatus; // 생산 입고 상태

    public static ProductionReceiving createProductionReceiving(User user, ProductionReceivingCreateRequest request) {
        ProductionReceiving productionReceiving = new ProductionReceiving(request);
        productionReceiving.specifyUser(user);

        return productionReceiving;
    }

    protected ProductionReceiving(ProductionReceivingCreateRequest request) {
        this.productionReceivingName = request.getProductionReceivingName();
        this.productionReceivingExtendedPrice = request.getProductionReceivingExtendedPrice();
        this.productionReceivingNote = request.getProductionReceivingNote();
        this.productionReceivingReceiptDate = request.getProductionReceivingReceiptDate();
        this.productionReceivingStatus = ProductionReceivingStatus.BEFORE;
    }

    private void specifyUser(User user) {
        this.user = user;
    }

    public void updateProductionReceiving(ProductionReceivingUpdateRequest request) {
        if(NullCheck.nullOrZeroCheck(request.getProductionReceivingExtendedPrice())) {
            this.productionReceivingExtendedPrice = request.getProductionReceivingExtendedPrice();
        }
        if(NullCheck.nullCheck(request.getProductionReceivingNote())) {
            this.productionReceivingNote = request.getProductionReceivingNote();
        }
        if(NullCheck.nullCheck(request.getProductionReceivingNote())) {
            this.productionReceivingReceiptDate = request.getProductionReceivingReceiptDate();
        }
    }

    // 결재 시 결재 후로 상태 변경
    public void updateProductionReceivingApproval() {
        this.productionReceivingStatus = ProductionReceivingStatus.AFTER;
    }

    // 생산입고 완료 시 생산완료로 상태 변경
    public void updateProductionReceivingComplete() {
        this.productionReceivingStatus = ProductionReceivingStatus.COMPLETE;
    }
}