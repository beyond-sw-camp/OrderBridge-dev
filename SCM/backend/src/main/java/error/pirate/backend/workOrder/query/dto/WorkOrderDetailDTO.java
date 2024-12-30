package error.pirate.backend.workOrder.query.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime workOrderIndicatedDate;
    private WorkOrderStatus workOrderStatus;
    private Integer workOrderIndicatedQuantity;
    private Integer workOrderPrice;
    private String userName;
    private String clientName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime workOrderRegDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime workOrderModDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime workOrderEndDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime workOrderDueDate;
    private Integer WorkOrderWorkQuantity;
    private String workOrderNote;

}
