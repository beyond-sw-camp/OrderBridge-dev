package error.pirate.backend.productionReceiving.command.domain.aggregate.entity;

import error.pirate.backend.common.NullCheck;
import error.pirate.backend.productionReceiving.command.application.dto.ProductionReceivingCreateRequest;
import error.pirate.backend.productionReceiving.command.application.dto.ProductionReceivingUpdateRequest;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_production_receiving") // 생산 입고
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductionReceiving {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productionReceivingSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "productionWarehouseSeq")
    private Warehouse productionWarehouse; // 생산 공장

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "storeWarehouseSeq")
    private Warehouse storeWarehouse; // 입고 창고

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user; // 생산 입고 관리자

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "workOrderSeq")
    private WorkOrder workOrder; // 작업지시서

    private String productionReceivingName; // 생산 입고명

    @CreatedDate
    private LocalDateTime productionReceivingRegDate; // 생산 입고 등록일

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime productionReceivingModDate; // 생산 입고 수정일

    private Integer productionReceivingExtendedPrice; // 생산 입고 총금액

    private String productionReceivingNote; // 생산 입고 비고

    @Enumerated(value = EnumType.STRING)
    private final ProductionReceivingStatus productionReceivingStatus = ProductionReceivingStatus.BEFORE; // 생산 입고 상태

    public static ProductionReceiving createProductionReceiving(Warehouse productionWarehouse, Warehouse storeWarehouse,
                                                                User user, WorkOrder workOrder,
                                                                ProductionReceivingCreateRequest request
                                                                ) {
        ProductionReceiving productionReceiving = new ProductionReceiving(request);
        productionReceiving.specifyProductionWarehouse(productionWarehouse);
        productionReceiving.specifyStoreWarehouse(storeWarehouse);
        productionReceiving.specifyUser(user);
        productionReceiving.specifyWorkOrder(workOrder);

        return productionReceiving;
    }

    protected ProductionReceiving(ProductionReceivingCreateRequest request) {
        this.productionReceivingName = request.getProductionReceivingName();
        this.productionReceivingExtendedPrice = request.getProductionReceivingExtendedPrice();
        this.productionReceivingNote = request.getProductionReceivingNote();
    }

    private void specifyProductionWarehouse(Warehouse productionWarehouse) {
        this.productionWarehouse = productionWarehouse;
    }

    private void specifyStoreWarehouse(Warehouse storeWarehouse) {
        this.storeWarehouse = storeWarehouse;
    }

    private void specifyUser(User user) {
        this.user = user;
    }

    private void specifyWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public void updateProductionReceiving(
            Warehouse productionWarehouse, Warehouse storeWarehouse,
            ProductionReceivingUpdateRequest request) {
        if(NullCheck.nullCheck(productionWarehouse)) {
           this.productionWarehouse = productionWarehouse;
        }
        if(NullCheck.nullCheck(storeWarehouse)) {
            this.storeWarehouse = storeWarehouse;
        }
        if(NullCheck.nullCheck(request.getProductionReceivingName())) {
            this.productionReceivingName = request.getProductionReceivingName();
        }
        if(NullCheck.nullOrZeroCheck(request.getProductionReceivingExtendedPrice())) {
            this.productionReceivingExtendedPrice = request.getProductionReceivingExtendedPrice();
        }
        if(NullCheck.nullCheck(request.getProductionReceivingNote())) {
            this.productionReceivingNote = request.getProductionReceivingNote();
        }
    }

}
