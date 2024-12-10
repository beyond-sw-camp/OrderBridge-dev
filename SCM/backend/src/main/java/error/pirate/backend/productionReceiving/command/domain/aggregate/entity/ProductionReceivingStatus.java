package error.pirate.backend.productionReceiving.command.domain.aggregate.entity;

public enum ProductionReceivingStatus {
    BEFORE, // 결재전
    AFTER,  // 결재후
    COMPLETE, // 생산입고 완료
    DELETE // 삭제
}
