package error.pirate.backend.purchaseOrder.command.application.dto;

import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderUpdateRequest {

    private Long purchaseOrderSeq;

    private Long clientSeq;  //거래처

    private Long SalesOrderSeq;  // 주문서

    private PurchaseOrderStatus purchaseOrderStatus = PurchaseOrderStatus.APPROVAL_BEFORE; // 발주서 상태

    private LocalDateTime purchaseOrderModDate; // 발주서 수정일

    private LocalDateTime purchaseOrderDueDate; // 발주서 계약 납기일

    private LocalDateTime purchaseOrderTargetDueDate; // 발주서 목표 납기일

    private Integer purchaseOrderExtendedPrice; // 발주서 총금액

    private String purchaseOrderNote; // 발주서 비고

    private List<PurchaseOrderItemDto> purchaseOrderItemDtoList;   // 발주서 품목 리스트

}
