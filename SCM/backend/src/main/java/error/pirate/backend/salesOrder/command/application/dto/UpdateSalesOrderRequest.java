package error.pirate.backend.salesOrder.command.application.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class UpdateSalesOrderRequest {
    private LocalDateTime salesOrderOrderDate;
    private LocalDateTime salesOrderDueDate;
    private Long clientSeq;
    private String salesOrderNote;
    private List<UpdateSalesOrderItemReqest> salesOrderItemReqestList;
}
