package error.pirate.backend.purchase.command.domain.aggregate.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PurchaseStatus {
    PROGRESS("정산중"),
    COMPLETE("정산완료"),
    DELETE("삭제");

    private final String value;
}
