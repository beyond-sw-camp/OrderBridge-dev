package error.pirate.backend.salesOrder.command.application.dto;

import lombok.Getter;

@Getter
public class SalesOrderItemRequest {
    private Long itemSeq;
    private Integer salesOrderItemQuantity;
    private Integer salesOrderItemPrice;
    private String salesOrderItemNote;
}
