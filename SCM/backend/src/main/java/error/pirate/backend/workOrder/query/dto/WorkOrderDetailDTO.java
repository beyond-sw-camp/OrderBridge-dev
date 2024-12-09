package error.pirate.backend.workOrder.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "작업지시서 상세조회 DTO")
public class WorkOrderDetailDTO {

    private Long workOrderSeq;
    private String workOrderName;
    private String warehouseName;
    private LocalDateTime workOrderIndicatedDate;
    private String workOrderStatus;
    private int workOrderIndicatedQuantity;
    private int workOrderPrice;
    private String userName;
    private String clientName;
    private LocalDateTime workOrderRegDate;
    private LocalDateTime workOrderModDate;
    private LocalDateTime workOrderEndDate;
    private LocalDateTime workOrderDueDate;
    private int WorkOrderWorkQuantity;
    private String workOrderNote;

}
