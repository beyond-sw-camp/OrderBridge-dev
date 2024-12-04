package error.pirate.backend.productionReceiving.command.domain.aggregate.entity;

import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_production_receiving") // 생산 입고
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductionReceiving {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
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
    @JoinColumn(name = "workOrdereq")
    private WorkOrder workOrder; // 작업지시서

    private String productionReceivingName; // 생산 입고명

    private LocalDateTime productionReceivingRegDate; // 생산 입고 등록일

    private LocalDateTime productionReceivingModDate; // 생산 입고 수정일

    private Integer productionReceivingExtendedPrice; // 생산 입고 총금액

    private String productionReceivingNote; // 생산 입고 비고
}
