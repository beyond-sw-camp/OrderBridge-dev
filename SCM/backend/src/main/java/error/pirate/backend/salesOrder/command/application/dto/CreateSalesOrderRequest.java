package error.pirate.backend.salesOrder.command.application.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CreateSalesOrderRequest {
    private LocalDateTime salesOrderOrderDate;
    private LocalDateTime salesOrderDueDate;
    private Long quotationSeq;
    private Long clientSeq;
    private String salesOrderNote;
    private List<SalesOrderItemRequest> salesOrderItemList;
}
