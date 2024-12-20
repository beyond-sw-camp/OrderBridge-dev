package error.pirate.backend.workOrder.query.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "작업지시서 현황 DTO")
public class WorkOrderSituationDTO {

    private String ItemName;
    private String ClientName;
    private String warehouseName;
    private String workOrderName;
    private int workOrderIndicatedQuantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime workOrderIndicatedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime workOrderDueDate;
    private String workOrderNote;

}
