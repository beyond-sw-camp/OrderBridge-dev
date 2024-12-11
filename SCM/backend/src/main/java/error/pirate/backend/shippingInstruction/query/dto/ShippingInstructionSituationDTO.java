package error.pirate.backend.shippingInstruction.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "출하지시서 현황 DTO")
public class ShippingInstructionSituationDTO {
    private long shippingInstructionSeq;    // 출하지시서 번호
    private LocalDate shippingInstructionScheduledShipmentDate; // 출하예정일
    private String shippingInstructionName; // 출하지시서명
    private int shippingInstructionTotalQuantity;   // 출하지시서 총수량
    private String clientName;  // 거래처명
    private String shippingInstructionAddress;  // 출하지시서 주소
    private String shippingInstructionNote; // 출하지시서 비고
}
