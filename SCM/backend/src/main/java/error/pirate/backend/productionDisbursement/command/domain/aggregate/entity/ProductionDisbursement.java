package error.pirate.backend.productionDisbursement.command.domain.aggregate.entity;

import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_production_disbursement") // 생산 불출
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ProductionDisbursement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productionDisbursementSeq;  // 생산불출 번호

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user; // 생산불출 담당자

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "workOrderSeq")
    private WorkOrder workOrder; // 작업지시서

    private String productionDisbursementName; // 생산불출명

    private String productionDisbursementTotalQuantity;  // 생산불출 총수량

    @CreatedDate
    private LocalDateTime productionDisbursementRegDate; // 생산불출 등록일

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime productionDisbursementModDate; // 생산불출 수정일

    private LocalDateTime productionDisbursementDepartureDate; // 생산불출 불출일

    @Enumerated(EnumType.STRING)
    private ProductionDisbursementStatus productionDisbursementStatus; // 생산불출 상태

    private String productionDisbursementNote; // 생산불출 비고


    public void deleteProductionDisbursement() {
        this.productionDisbursementStatus = ProductionDisbursementStatus.DELETE;
    }
}
