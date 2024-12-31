package error.pirate.backend.purchaseOrder.query.dto;

import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PurchaseOrderResponse {

    private Long purchaseOrderSeq;

    private String userName; // 회원 이름

    private String userPhoneNo;

    private String clientName; // 거래처 이름

    private String purchaseOrderName; // 발주서명

    private PurchaseOrderStatus purchaseOrderStatus; // 발주서 상태

    @Setter
    private String purchaseOrderStatusValue;

    private LocalDateTime purchaseOrderDueDate; // 발주서 계약 납기일

    private LocalDateTime purchaseOrderTargetDueDate; // 발주서 목표 납기일

    private Integer purchaseOrderExtendedPrice; // 발주서 총금액

    private String purchaseOrderNote; // 발주서 비고

    private Integer purchaseOrderTotalQuantity;  // 총 수량

    @Setter
    private List<PurchaseOrderItemResponse> purchaseOrderItemResponseList;

}
