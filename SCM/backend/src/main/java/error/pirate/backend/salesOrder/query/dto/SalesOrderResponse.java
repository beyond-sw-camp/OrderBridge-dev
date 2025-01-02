package error.pirate.backend.salesOrder.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SalesOrderResponse {
    private Long salesOrderSeq;
    private String salesOrderName;
    private String clientName;
    private Integer salesOrderExtendedPrice;
    private Integer salesOrderTotalQuantity;
    private String userName;
    private String userPhoneNo;
    private LocalDateTime salesOrderOrderDate;
    private LocalDateTime salesOrderDueDate;
    private String salesOrderNote;
    @Setter
    private List<SalesOrderItemDTO> salesOrderItem;
}
