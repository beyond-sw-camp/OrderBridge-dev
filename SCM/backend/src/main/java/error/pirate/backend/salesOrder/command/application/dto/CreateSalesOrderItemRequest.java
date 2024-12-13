package error.pirate.backend.salesOrder.command.application.dto;

import lombok.Getter;

@Getter
public class CreateSalesOrderItemRequest {
    private Long itemSeq;
    private Integer salesOrderItemQuantity;
    private Integer salesOrderItemPrice;
    private String salesOrderItemNote;
}
