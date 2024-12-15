package error.pirate.backend.salesOrder.command.domain.aggregate.entity;

public enum SalesOrderStatus {
    BEFORE, // 결재전
    AFTER, // 결재후
    PRODUCTION, // 생산중
    PRODUCTION_COMPLETE,   // 생산완료
    SHIPMENT_COMPLETE,
    DELETE // 출하완료
}
