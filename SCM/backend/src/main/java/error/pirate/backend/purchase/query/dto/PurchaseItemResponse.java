package error.pirate.backend.purchase.query.dto;

import error.pirate.backend.purchase.command.domain.aggregate.entity.PurchaseItemStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PurchaseItemResponse {

    private Long purchaseItemSeq;

    private Long itemSeq; // 품목 번호

    private String itemName;  // 품목 이름

    private String itemImageUrl;  // 품목 사진 url

    private int purchaseItemQuantity; // 구매서 품목 수량

    private int purchaseItemPrice; // 구매서 품목 단가

    private LocalDateTime purchaseItemReceiptDate;  // 구매 품목 입고일

    private PurchaseItemStatus purchaseItemStatus;  // 구매 품목 상태

    private String purchaseItemNote; // 구매서 품목 비고

}
