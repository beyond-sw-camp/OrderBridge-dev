package error.pirate.backend.shippingInstruction.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "출하지시서 현황 응답")
public class ShippingInstructionSituationResponse {
    private long shippingInstructionSeq;    // 출하지시서 번호
    private LocalDateTime shippingInstructionScheduledShipmentDate; // 출하예정일
    private String shippingInstructionScheduledShipmentMonthDate; // 월별 출하예정일
    private String shippingInstructionName; // 출하지시서명
    private String shippingInstructionTotalQuantity;   // 출하지시서 총수량
    private String clientName;  // 거래처명
    private String shippingInstructionAddress;  // 출하지시서 주소
    private String shippingInstructionNote; // 출하지시서 비고
    private String shippingInstructionTotalQuantitySum;  // 출하지시서 총수량 월합계 총합계
}
