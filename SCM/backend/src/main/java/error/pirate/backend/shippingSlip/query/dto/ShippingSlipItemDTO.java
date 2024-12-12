package error.pirate.backend.shippingSlip.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "출하전표 품목 DTO")
public class ShippingSlipItemDTO {
    private long shippingSlipItemSeq;    // 출하전표 품목 번호
    private long itemSeq;   // 품목 번호
    private String itemImageUrl;    // 품목 이미지 url
    private String itemName;    // 품목명
    private String itemDivision;    // 품목 구분
    private int itemPrice;  // 품목 단가
    private int shippingSlipItemQuantity;    // 출하전표 품목 수량
    private String shippingSlipItemNote; // 출하전표 품목 비고
    private int itemTotalAmount;  // 품목 총금액
}
