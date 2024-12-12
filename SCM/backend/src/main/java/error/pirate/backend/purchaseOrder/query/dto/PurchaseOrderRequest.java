package error.pirate.backend.purchaseOrder.query.dto;

import error.pirate.backend.common.Pagination;
import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrderStatus;

import java.time.LocalDateTime;

public class PurchaseOrderRequest extends Pagination {

    private String clientName;  // 거래처명

    private LocalDateTime purchaseOrderTargetDueDate;   // 발주서 목표 납기일

    private PurchaseOrderStatus purchaseOrderStatus;

}
