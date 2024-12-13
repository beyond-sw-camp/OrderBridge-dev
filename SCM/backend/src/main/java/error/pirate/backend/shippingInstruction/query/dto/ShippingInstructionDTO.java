package error.pirate.backend.shippingInstruction.query.dto;

import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstructionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "출하지시서 DTO")
public class ShippingInstructionDTO {
    private long shippingInstructionSeq;    // 출하지시서 번호
    private String shippingInstructionName; // 출하지시서명
    private ShippingInstructionStatus shippingInstructionStatus;   // 출하지시서 상태
    private LocalDateTime shippingInstructionScheduledShipmentDate; // 출하예정일
    private String clientName;  // 거래처명
    private int shippingInstructionTotalQuantity;   // 출하지시서 총수량
    private String shippingInstructionAddress;  // 출하지시서 주소
    private String userName;    // 출하지시서 담당자
    private String shippingInstructionNote; // 출하지시서 비고
}
