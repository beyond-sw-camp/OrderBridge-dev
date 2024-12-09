package error.pirate.backend.salesOrder.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class SalesOrderListItemDTO {
    private Long salesOrderSeq;
    private String salesOrderName;
    private String itemName;
    private String clientName;
    private LocalDate salesOrderOrderDate;
    private String salesOrderStatus;
}
