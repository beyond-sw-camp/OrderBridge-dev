package error.pirate.backend.purchaseOrder.query.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PurchaseOrderSituationResponse {

    private Long purchaseOrderSeq;

     private String clientName; // 거래처 이름

    private LocalDateTime purchaseOrderRegDate; // 발주 일자

    private Integer purchaseOrderItemExtendedPrice;  // 발주 품목 총금액

    private Integer purchaseOrderExtendedPrice;  // 발주 월별 총금액

    private Integer purchaseOrderTotalQuantity;   // 발주 월별 총수량

    private String purchaseOrderRegMonth;

    private String itemName;  // 품목 이름

    private int purchaseOrderItemQuantity; // 발주서 품목 수량

    private int purchaseOrderItemPrice; // 발주서 품목 단가

    private String purchaseOrderItemNote; // 발주서 품목 비고

}
