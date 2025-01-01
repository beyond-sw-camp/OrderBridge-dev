package error.pirate.backend.salesOrder.query.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SalesOrderSituationExcelDTO {
    private LocalDate salesOrderOrderDate;
    private String salesOrderName;
    private Integer salesOrderTotalQuantity;
    private Integer salesOrderExtendedPrice;
    private String clientName;
    private String salesOrderNote;
}
