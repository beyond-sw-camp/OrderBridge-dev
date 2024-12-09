package error.pirate.backend.purchaseOrder.command.domain.aggregate.entity;

public enum PurchaseOrderStatus {
    BEFORE, // 구매전
    AFTER,  // 구매후
    REFUSAL // 반려
}
