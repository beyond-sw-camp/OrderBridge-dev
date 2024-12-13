package error.pirate.backend.workOrder.command.domain.aggregate.entity;

import error.pirate.backend.client.command.domain.aggregate.entity.Client;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.workOrder.command.application.dto.CreateWorkOrderRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_work_order") // 작업지시서
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tb_work_order SET work_order_status = 'DELETE' WHERE work_order_seq = ? AND work_order_status = 'BEFORE'")
public class WorkOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workOrderSeq;
    
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "clientSeq")
    private Client client; // 거래처(납품처)

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user; // 작업지시서 담당자

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "salesOrderSeq")
    private SalesOrder salesOrder; // 주문서

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseSeq")
    private Warehouse warehouse; // 창고

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "itemSeq")
    private Item item; // 품목

    private String workOrderName; // 작업지시서 명

    @CreatedDate
    private LocalDateTime workOrderRegDate; // 작업지시서 등록일

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime workOrderModDate; // 작업지시서 수정일

    private LocalDateTime workOrderIndicatedDate; // 작업지시서 작업 지시일

    private LocalDateTime workOrderEndDate; // 작업지시서 작업 완료일

    private LocalDateTime workOrderDueDate; // 작업지시서 납기일

    private Integer workOrderIndicatedQuantity; // 작업지시서 주문수량

    private Integer workOrderWorkQuantity; // 작업지시서 작업완료수량

    @Enumerated(EnumType.STRING)
    private WorkOrderStatus workOrderStatus; // 작업지시서 상태

    private Integer workOrderPrice; // 작업지시서 총금액

    private String workOrderNote; // 작업지시서 비고

    public static WorkOrder createWorkOrder(CreateWorkOrderRequest request, SalesOrder salesOrder, SalesOrderItem salesOrderItem,
                                            Warehouse warehouse, User user,
                                            LocalDateTime seoulIndicatedDate, LocalDateTime seoulDueDate) {
        if (seoulDueDate.isBefore(seoulIndicatedDate)) {
            throw new CustomException(ErrorCodeType.INVALID_DATE_RANGE);
        }
        WorkOrder workOrder = new WorkOrder();

        // SalesOrder 기반 설정
        workOrder.salesOrder = salesOrder;
        workOrder.client = salesOrder.getClient(); // 클라이언트 설정
//        workOrder.workOrderIndicatedQuantity = salesOrder.getSalesOrderTotalQuantity(); // 지시 수량 설정
        workOrder.workOrderIndicatedQuantity = salesOrderItem.getSalesOrderItemQuantity(); // 지시 수량 설정
        workOrder.workOrderPrice = salesOrder.getSalesOrderExtendedPrice(); // 금액 설정
        workOrder.specifyItem(salesOrderItem.getItem()); // 품목 설정

        workOrder.warehouse = warehouse;
        workOrder.user = user;
        workOrder.workOrderName = request.getWorkOrderName();
        workOrder.workOrderStatus = WorkOrderStatus.valueOf("BEFORE");
        workOrder.workOrderWorkQuantity = 0;
        workOrder.workOrderIndicatedDate = seoulIndicatedDate;
        workOrder.workOrderDueDate = seoulDueDate;
        workOrder.workOrderNote = request.getWorkOrderNote();
        return workOrder;
    }

    private void specifyItem(Item item) {
        this.item = item;
    }

}
