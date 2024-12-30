package error.pirate.backend.productionDisbursement.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@Schema(description = "생산불출 현황 응답 DTO")
public class ProductionDisbursementSituationResponse {

    private List<MonthlyProductionDisbursementSituationDTO> monthlySituations;   // 월별 생산불출 현황
    private int totalQuantity;  // 전체 생산불출 수량
}
