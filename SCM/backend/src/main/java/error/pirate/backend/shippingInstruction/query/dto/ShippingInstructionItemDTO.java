package error.pirate.backend.shippingInstruction.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "출하지시서 품목 DTO")
public class ShippingInstructionItemDTO {
    private long shippingInstructionItemSeq;    // 출하지시서 품목 번호
    private long itemSeq;   // 품목 번호
    private String itemImageUrl;    // 품목 이미지 url
    private String itemName;    // 품목명
    private String itemDivision;    // 품목 구분
    private int shippingInstructionItemQuantity;    // 출하지시서 품목 수량
    private String shippingInstructionItemNote; // 출하지시서 품목 비고
}
