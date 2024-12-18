package error.pirate.backend.shippingInstruction.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "출하지시서 품목 DTO")
public class ShippingInstructionItemRequest {
    private long itemSeq;    // 품목 시퀀스
    private int shippingInstructionItemQuantity;    // 물품 수량
    private String shippingInstructionItemNote;     // 물품 비고
}
