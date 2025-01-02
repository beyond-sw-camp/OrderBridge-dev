package error.pirate.backend.salesOrder.query.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SalesOrderSituationResponse {
    private LocalDate salesOrderOrderDate;
    private String salesOrderName;
    private Integer salesOrderTotalQuantity;
    private Integer salesOrderExtendedPrice;
    private String clientName;
    private String salesOrderNote;

    private String salesOrderOrderMonte;
    private Integer salesOrderMonthPrice;
    private Integer salesOrderMonthQuantity;
}
