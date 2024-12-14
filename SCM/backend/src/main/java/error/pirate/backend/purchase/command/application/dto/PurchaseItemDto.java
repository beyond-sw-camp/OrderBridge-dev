package error.pirate.backend.purchase.command.application.dto;

import error.pirate.backend.purchase.command.domain.aggregate.entity.PurchaseItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseItemDto {

    private Long purchaseItemSeq;

    private Long itemSeq;

    private Long purchaseSeq;   // 구매서 번호

    private int purchaseItemQuantity; // 구매 품목 수량

    private int purchaseItemPrice; // 구매 품목 단가

    private LocalDateTime purchaseItemReceiptDate; // 구매 품목 입고일

    private PurchaseItemStatus purchaseItemStatus; // 구매서 상태

    private String purchaseItemNote; // 구매 품목 비고

}
