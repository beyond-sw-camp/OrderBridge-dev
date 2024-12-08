package error.pirate.backend.salesOrder.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class SalesOrderSituationDTO {
    private Long salesOrderSeq;
    private LocalDate salesOrderOrderDate;
    private String salesOrderName;
    private Integer salesOrderTotalQuantity;
    private Integer salesOrderExtendedPrice;
    private String clientName;
    private String salesOrderNote;
}
