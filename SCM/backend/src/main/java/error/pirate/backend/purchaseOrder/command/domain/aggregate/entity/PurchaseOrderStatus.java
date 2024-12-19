package error.pirate.backend.purchaseOrder.command.domain.aggregate.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PurchaseOrderStatus {
    APPROVAL_BEFORE("결재전"),
    APPROVAL_AFTER("결재후"),
    APPROVAL_REFUSAL("반려"),
    APPROVAL_COMPLETE("구매완료"),
    DELETE("삭제");

    private final String value;

}
