package error.pirate.backend.purchaseOrder.command.domain.aggregate.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PurchaseOrderStatus {
    APPROVAL_BEFORE("구매전"),
    APPROVAL_AFTER("구매후"),
    APPROVAL_REFUSAL("반려");

    private final String value;

}
