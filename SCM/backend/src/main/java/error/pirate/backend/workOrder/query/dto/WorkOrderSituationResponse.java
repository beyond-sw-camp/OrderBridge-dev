package error.pirate.backend.workOrder.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@Schema(description = "작업지시서 현황 응답 DTO")
public class WorkOrderSituationResponse {

    private List<MonthlyWorkOrderSituationDTO> monthlySituations;   // 월별 작업지시서 현황
    private int totalQuantity;  // 전체 작업지시 수량
}
