package error.pirate.backend.purchase.command.domain.aggregate.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PurchaseItemStatus {
    DELIVERY_BEFORE("정산중"),
    DELIVERY_COMPLETE("정산완료")
    ;

    private final String value;
}
