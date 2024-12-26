package error.pirate.backend.purchaseOrder.query.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PurchaseOrderSituationResponse {

    private Long purchaseOrderSeq;

     private String clientName; // 거래처 이름

    private String purchaseOrderName; // 발주서명

    private LocalDateTime purchaseOrderRegDate; // 발주 일자

    private LocalDateTime purchaseOrderTargetDueDate; // 발주 목표 납기일

    private Integer purchaseOrderExtendedPrice;  // 발주 품목 총금액

    private Integer purchaseOrderTotalQuantity;

    private Integer purchaseOrderMonthPrice;  // 발주 월별 총금액

    private Integer purchaseOrderMonthQuantity;   // 발주 월별 총수량

    private String purchaseOrderRegMonth;

    private String purchaseOrderNote; // 발주서 비고

}
