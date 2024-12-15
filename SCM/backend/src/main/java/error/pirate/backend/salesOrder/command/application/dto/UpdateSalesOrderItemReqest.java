package error.pirate.backend.salesOrder.command.application.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UpdateSalesOrderItemReqest {
    private Long itemSeq;
    private Integer salesOrderItemQuantity;
    private Integer salesOrderItemPrice;
    private String salesOrderItemNote;
}
