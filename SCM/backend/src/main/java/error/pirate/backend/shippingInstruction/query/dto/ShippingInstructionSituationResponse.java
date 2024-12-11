package error.pirate.backend.shippingInstruction.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "출하지시서 현황 응답")
public class ShippingInstructionSituationResponse {
    private List<ShippingInstructionSituationDTO> shippingInstructionSituationList;
    private List<ShippingInstructionSituationMonthlyTotalDTO> monthlyTotalList;   // 월합계 리스트
    private int totalQuantity;  // 총합계
}
