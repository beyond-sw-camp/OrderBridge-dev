package error.pirate.backend.salesOrder.query.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SalesOrderDTO {
    private Long salesOrderSeq;
    private String salesOrderName;
    private String clientName;
    private Integer salesOrderExtendedPrice;
    private Integer salesOrderTotalQuantity;
    private String userName;
    private LocalDateTime salesOrderOrderDate;
    private LocalDateTime salesOrderDueDate;
    private String salesOrderNote;
}
