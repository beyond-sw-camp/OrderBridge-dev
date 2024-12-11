package error.pirate.backend.workOrder.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@Schema(description = "월별 작업지시서 현황 DTO")
public class MonthlyWorkOrderSituationDTO {

    private String month;
    private List<WorkOrderSituationDTO> situations; // 해당 월 작업지시서 목록
    private int monthlyTotalQuantity;   // 해당 월 총 수량

}
