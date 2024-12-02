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

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_production_disbursement") // 생산불출
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductionDisbursement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productionDisbursementId;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user; // 생산불출 담당자

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseSeq")
    private Warehouse ingredientsWarehouse; // 원자재 창고

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseSeq")
    private Warehouse productionWarehouse; // 생산 공장

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "workOrderSeq")
    private WorkOrder workOrder; // 작업지시서

    @CreatedDate
    private LocalDateTime productionDisbursementRegDate; // 생산불출 등록일

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime productionDisbursementModDate; // 생산불출 수정일

    private LocalDateTime productionDisbursementDepartureDate; // 생산불출 불출일

    private ProductionDisbursementStatus productionDisbursementStatus; // 생산불출 상태

    private String productionDisbursementNote; // 생산불출 비고
}
