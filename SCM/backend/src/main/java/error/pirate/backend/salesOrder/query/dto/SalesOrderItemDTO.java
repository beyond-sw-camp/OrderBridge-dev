package error.pirate.backend.salesOrder.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SalesOrderItemDTO {
    private Long salesOrderItemSeq;
    private Long itemSeq;
    private String itemImageUrl;
    private String itemDivision;
    private String itemName;
    private Integer salesOrderItemQuantity;
    private Integer salesOrderItemPrice;
    private String salesOrderItemNote;
}
