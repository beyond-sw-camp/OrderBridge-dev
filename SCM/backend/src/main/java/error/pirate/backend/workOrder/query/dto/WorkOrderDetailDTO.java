package error.pirate.backend.workOrder.query.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "작업지시서 상세조회 DTO")
public class WorkOrderDetailDTO {

    private Long workOrderSeq;
    private Long salesOrderSeq;
    private String workOrderName;
    private Long warehouseSeq;
    private String warehouseName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime workOrderIndicatedDate;
    private WorkOrderStatus workOrderStatus;
    private Integer workOrderIndicatedQuantity;
    private Integer workOrderPrice;
    private String userName;
    private String clientName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime workOrderRegDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime workOrderModDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime workOrderEndDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime workOrderDueDate;
    private Integer WorkOrderWorkQuantity;
    private String workOrderNote;

}
