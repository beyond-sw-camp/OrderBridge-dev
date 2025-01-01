package error.pirate.backend.salesOrder.query.dto;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SalesOrderExcelDTO {
    private LocalDateTime salesOrderRegDate;
    private LocalDateTime salesOrderModDate;
    private String salesOrderName;
    private SalesOrderStatus salesOrderStatus;
    private LocalDateTime salesOrderOrderDate;
    private LocalDateTime salesOrderDueDate;
    private Integer salesOrderTotalQuantity;
    private Integer salesOrderExtendedPrice;
    private String salesOrderNote;
}
