package error.pirate.backend.shippingInstruction.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "출하지시서 현황 DTO")
public class ShippingInstructionSituationDTO {
    private long shippingInstructionSeq;
    private LocalDate shippingInstructionScheduledShipmentDate;
    private String shippingInstructionName;
    private int shippingInstructionTotalQuantity;
    private String clientName;
    private String shippingInstructionAddress;
    private String shippingInstructionNote;
}
