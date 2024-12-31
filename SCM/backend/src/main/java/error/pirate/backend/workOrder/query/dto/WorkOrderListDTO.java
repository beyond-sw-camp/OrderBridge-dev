package error.pirate.backend.workOrder.query.dto;

import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Schema(description = "작업지시서 목록")
public class WorkOrderListDTO {

    private Long workOrderSeq;
    private String workOrderName;
    private WorkOrderStatus workOrderStatus;
    private LocalDateTime workOrderIndicatedDate;
    private LocalDateTime workOrderDueDate;
    private Long warehouseSeq;
    private String warehouseName;
    private String itemName;
    private Long itemSeq;

}
