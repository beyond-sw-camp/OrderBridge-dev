package error.pirate.backend.shippingInstruction.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Schema(description = "출하지시서 상세 응답")
public class ShippingInstructionResponse {
    private long shippingInstructionSeq;
    private String shippingInstructionName;
    private String shippingInstructionStatus;
    private LocalDateTime shippingInstructionScheduledShipmentDate;
    private String clientName;

    private int shippingInstructionTotalQuantity;
    private String shippingInstructionAddress;
    private String shippingInstructionNote;

    private List<ItemDTO> itemList;
}
