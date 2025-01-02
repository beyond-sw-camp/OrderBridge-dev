package error.pirate.backend.salesOrder.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesOrderItemStockStatusResponse {

    private Long salesOrderItemSeq;   // 주문서 품목 번호
    private Long itemSeq;             // 품목 번호
    private String itemName;            // 품목 이름
    private String itemImageUrl;
    private Integer requiredQuantity; // 필요 수량 (주문 수량)
    private Integer availableQuantity; // 생산 가능한 수량
    private Integer insufficientQuantity; // 부족한 수량 (필요 수량 - 가능 수량)
    private Boolean isStockEnough;   // 재고 충분 여부
    private String salesOrderItemNote;    // 품목 비고

    public void calculateStockStatus() {
        this.isStockEnough = availableQuantity >= requiredQuantity;
        this.insufficientQuantity = (availableQuantity >= requiredQuantity)
                ? 0
                : requiredQuantity - availableQuantity;
    }
}
