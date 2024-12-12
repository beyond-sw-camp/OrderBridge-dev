package error.pirate.backend.shippingInstruction.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "출하지시서 월합계 DTO")
public class ShippingInstructionSituationMonthlyTotalDTO {
    private String month;  // 월
    private int monthlyTotal;   // 월합계
}
