package error.pirate.backend.shippingInstruction.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "출하지시서 리스트 DTO")
public class ShippingInstructionListDTO {
    private long shippingInstructionSeq;    // 출하지시서 번호
    private String shippingInstructionName; // 출하지시서명
    private String shippingInstructionStatus;   // 출하지시서 상태
    private LocalDateTime shippingInstructionScheduledShipmentDate; // 출하예정일
    private String clientName;  // 거래처명
    private String itemName;    // 품목명
}
