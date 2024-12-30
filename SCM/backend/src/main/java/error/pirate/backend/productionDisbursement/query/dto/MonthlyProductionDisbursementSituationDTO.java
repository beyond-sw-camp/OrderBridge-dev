package error.pirate.backend.productionDisbursement.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@Schema(description = "월별 생산불출 현황 DTO")
public class MonthlyProductionDisbursementSituationDTO {

    private String month;
    private List<ProductionDisbursementSituationDTO> situations; // 해당 월 생산불출 목록
    private int monthlyTotalQuantity;   // 해당 월 총 수량
}
