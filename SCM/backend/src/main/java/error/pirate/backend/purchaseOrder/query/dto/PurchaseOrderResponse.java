package error.pirate.backend.purchaseOrder.query.dto;

import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PurchaseOrderResponse {

    private Long purchaseOrderSeq;

    private String userName; // 회원 이름

    private String clientName; // 거래처 이름

    private String purchaseOrderName; // 발주서명

    private PurchaseOrderStatus purchaseOrderStatus; // 발주서 상태

    private LocalDateTime purchaseOrderDueDate; // 발주서 계약 납기일

    private LocalDateTime purchaseOrderTargetDueDate; // 발주서 목표 납기일

    private Integer purchaseOrderExtendedPrice; // 발주서 총금액

    private String purchaseOrderNote; // 발주서 비고

    // purchaseOrder item 관련
    private Long purchaseOrderItemSeq;

    private Long itemSeq; // 품목 번호

    private String itemName;  // 품목 이름

    private String itemImageUrl;  // 품목 사진 url

    private int purchaseOrderItemQuantity; // 발주서 품목 수량

    private int purchaseOrderItemPrice; // 발주서 품목 단가

    private String purchaseOrderItemNote; // 발주서 품목 비고

}
