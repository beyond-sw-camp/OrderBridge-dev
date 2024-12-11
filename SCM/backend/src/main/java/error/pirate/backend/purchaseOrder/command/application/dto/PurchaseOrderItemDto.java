package error.pirate.backend.purchaseOrder.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemDto {

    private Long purchaseOrderItemSeq;

    private Long itemSeq;

    private String itemName;

    private int purchaseOrderItemQuantity; // 발주서 품목 수량

    private int purchaseOrderItemPrice; // 발주서 품목 단가

    private String purchaseOrderItemNote; // 발주서 품목 비고

}
