package error.pirate.backend.shippingSlip.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "출하전표 품목 DTO")
public class ShippingSlipItemDTO {
    private long itemSeq;    // 품목 시퀀스
    private int shippingSlipItemQuantity;    // 물품 수량
    private String shippingSlipItemNote;     // 물품 비고
}
