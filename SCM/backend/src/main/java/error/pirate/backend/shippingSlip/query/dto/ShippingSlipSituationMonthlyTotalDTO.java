package error.pirate.backend.shippingSlip.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "출하전표 월합계 DTO")
public class ShippingSlipSituationMonthlyTotalDTO {
    private String month;  // 월
    private int monthlyTotal;   // 월합계
}
