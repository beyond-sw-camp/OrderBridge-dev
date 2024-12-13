package error.pirate.backend.purchaseOrder.query.dto;

import lombok.Getter;

@Getter
public class PurchaseOrderItemResponse {

    private Long purchaseOrderItemSeq;

    private Long itemSeq; // 품목 번호

    private String itemName;  // 품목 이름

    private String itemImageUrl;  // 품목 사진 url

    private int purchaseOrderItemQuantity; // 발주서 품목 수량

    private int purchaseOrderItemPrice; // 발주서 품목 단가

    private String purchaseOrderItemNote; // 발주서 품목 비고

}
