package error.pirate.backend.workOrder.query.dto;

import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderRequest;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WorkOrderListExcelDTO {

    private String workOrderName;
    private String itemName;
    private String workOrderIndicatedQuantity;
    private String warehouseName;
    private LocalDateTime workOrderIndicatedDate;
    private WorkOrderStatus workOrderStatus;

}
