package error.pirate.backend.purchase.command.domain.aggregate.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PurchaseItemStatus {
    PROGRESS("정산중"),
    COMPLETE("정산완료")
    ;

    private final String value;
}
